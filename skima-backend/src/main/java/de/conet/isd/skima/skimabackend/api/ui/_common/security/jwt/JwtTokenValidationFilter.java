package de.conet.isd.skima.skimabackend.api.ui._common.security.jwt;

import de.conet.isd.skima.skimabackend.api.ui._common.security.CurrentUserInfo;
import de.conet.isd.skima.skimabackend.domain.entities.users.SkimaPermission;
import de.conet.isd.skima.skimabackend.service.security.RolePermissionResolver;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class JwtTokenValidationFilter extends OncePerRequestFilter {

    /**
     * Thrown internally, when a JWT is parsable and valid but in an unexpected format
     * (that happens, when our JWT Claim Structure changes in a non-backwards-compatible way, for example)
     */
    public static class ClaimParsingError extends RuntimeException {
        public ClaimParsingError(String message) {
            super(message);
        }

        public ClaimParsingError(Throwable cause) {
            super(cause);
        }
    }

    private final Logger log = LoggerFactory.getLogger(JwtTokenValidationFilter.class);

    private final JwtConfig jwtConfig;
    private final RolePermissionResolver rolePermissionResolver;

    public JwtTokenValidationFilter(JwtConfig jwtConfig, RolePermissionResolver rolePermissionResolver) {
        this.jwtConfig = jwtConfig;
        this.rolePermissionResolver = rolePermissionResolver;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        /*
         * authenticateRequest() will set the Authentication Object in the current SecurityContext
         * on successful authentication, otherwise the Auth Object will remain empty, causing the
         * Sprint Security Filterchain to fail the request as Unauthorized.
         */
        authenticateRequest(request, response);
        filterChain.doFilter(request, response);
    }

    private void authenticateRequest(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        String authorizationHeader = request.getHeader("Authorization");

        final String tokenString;
        // No token provided => no authentication
        if ((authorizationHeader == null || !authorizationHeader.startsWith("Bearer"))) {
            if (isQueryParamFallback(request)) {
                // fallback to query param:
                log.warn("Using URI-Queryparams to transfer token as Authentication Fallback mechanism.");
                tokenString = request.getParameter("token");
            } else {
                // Unauthenticated:

                /* Not calling propagateSuccessfulAuthentication() and thus
                 * not setting the Authentication Object in the Security Context
                 * will lead to the Request being handled as unauthenticated
                 * in the next steps in the filterchain.
                 */

                // be extra-sure to make the request "unauthenticated":
                SecurityContextHolder.getContext().setAuthentication(null);

                return;
            }
        } else {
            tokenString = authorizationHeader.replace("Bearer ", "");
        }

        try {
            authenticateToken(tokenString);
        } catch (JwtException | ClaimParsingError e) {
            log.warn("Blocked Invalid Token {}", tokenString);
            // be extra-sure to make the request "unauthenticated":
            SecurityContextHolder.getContext().setAuthentication(null);
        }
    }

    private boolean isQueryParamFallback(HttpServletRequest request) {
        return false;
//        String url = request.getServletPath();
//        if (!(url.contains("special-url"))
//        ) {
//            return false;
//        }
//        String token = request.getParameter("token");
//        return token != null && !token.isEmpty();
    }

    public void authenticateToken(String tokenString) throws JwtException, ClaimParsingError {
        Jws<Claims> claims = validate(tokenString);
        propagateSuccessfulAuthentication(tokenString, claims);
    }

    public Jws<Claims> validate(String token) {
        return Jwts.parser()
                .verifyWith(jwtConfig.getKey())
                .build()
                .parseSignedClaims(token);
    }

    private void propagateSuccessfulAuthentication(String tokenString, Jws<Claims> claims) throws ClaimParsingError {
        //
        // Assemble Current User Info from Claims:
        //
        // /!\/!\/!\/!\/!\/!\/!\/!\/!\/!\/!\/!\/!\/!\/!\/!\/!\/!\/!\/!\/!\/!\/!\/!\/!\
        //
        //      !!! DO NOT TOUCH THE DATABASE,
        //          FETCH OTHER RESOURCES,
        //          OR DO ANYTHING ELSE THAT MIGHT
        //          TAKE LONGER THAN MICROSECONDS !!!
        //
        // This Method gets called on EVERY single HTTP Request.
        // It must remain a lightweight filter.
        //
        // If you need more Data within CurrentUserInfo, add additional claims.
        // (Note: this is only adequate for data of crosscutting concerns that you use in many features;
        //  if you miss a user field for a single or very few features, do not add those field
        //  to the token, but fetch the user object from database *within that feature* (i.e.: not here!))
        //
        // /!\/!\/!\/!\/!\/!\/!\/!\/!\/!\/!\/!\/!\/!\/!\/!\/!\/!\/!\/!\/!\/!\/!\/!\/!\
        //

        Set<String> roles = new HashSet<String>(claims.getPayload().get(JwtClaims.ROLES, List.class));
        Set<SkimaPermission> permissions = rolePermissionResolver.resolve(roles); // must work cached

        Authentication currentUserInfo = new CurrentUserInfo(
                claims.getPayload().getSubject(),
                tokenString,
                claims.getPayload().get(JwtClaims.USER_ID, Long.class),
                roles,
                permissions
        );

        SecurityContextHolder.getContext()
                .setAuthentication(currentUserInfo); // <-- /!\ this propagates successful auth in Spring context /!\
    }


    private <T extends Enum<T>> Set<T> mapEnumListClaim(Jws<Claims> claims, String claimName, Class<T> enumClazz) {
        List<String> claim = claims.getPayload().get(claimName, List.class);
        if (claim == null) {
            log.warn("JWT had an old structure or failed to parse for other reason: no '{}' claim", claimName);
            throw new ClaimParsingError("No 'Departments' Claim");
        }

        return claim.stream()
                .filter(Objects::nonNull)
                .map(name -> {
                    try {
                        return Enum.valueOf(enumClazz, name);
                    } catch (IllegalArgumentException e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }


}
