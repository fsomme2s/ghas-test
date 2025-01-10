package de.conet.isd.skima.skimabackend.api.ui._common.security.jwt;

import de.conet.isd.skima.skimabackend.api.ui._common.security.spi.AuthenticationResult;
import de.conet.isd.skima.skimabackend.api.ui._common.security.spi.SkimaAuthenticator;
import de.conet.isd.skima.skimabackend.domain.entities.users.SkimaUser;
import de.conet.isd.skima.skimabackend.service.users.UserRepo;
import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Set;

@Component
@Primary // Required for Testsuits where an inherited Version of this Jwt Factory exists
public class JwtLoginTokenFactory {
    private final Logger log = LoggerFactory.getLogger(JwtLoginTokenFactory.class);

    private final SkimaAuthenticator authenticator;
    private final JwtConfig jwtConfig;
    private final UserRepo userRepo;

    public JwtLoginTokenFactory(SkimaAuthenticator authenticator, JwtConfig jwtConfig, UserRepo userRepo) {
        this.authenticator = authenticator;
        this.jwtConfig = jwtConfig;
        this.userRepo = userRepo;
    }

    public String attemptLogin(String username, String password) throws AuthenticationException {
        AuthenticationResult authResult = authenticator.authenticate(username, password);
        if (authResult.authenticated()) {
            SkimaUser user = userRepo.findByUsernameFetchAuthorizationInfos(username);
            if (user == null) {
                // Authenticated but does not exist = create new one
                log.info("User '{}' was authenticated but not yet present in database - creating new account", username);
                user = new SkimaUser();
                user.setUsername(username);
                user = userRepo.save(user);
//            } else {
                // TODO: log successful login? check if required by security or forbidden by data protection rules!
            }
            return createToken(user, authResult.roles());
        } else {
            throw new BadCredentialsException("Authentication failed");
        }
    }

    public String createToken(SkimaUser user, Set<String> roles) {
        return Jwts.builder()
                .subject(user.getUsername())
                .claim(JwtClaims.USER_ID, user.getId())
                .claim(JwtClaims.ROLES, roles)
                .issuedAt(new Date())
                .expiration(Date.from(Instant.now().plus(jwtConfig.tokenExpirationAfterMinutes(), ChronoUnit.MINUTES)))
                .signWith(jwtConfig.getKey())
                .compact();
    }

}
