package de.conet.isd.skima.skimabackend.api.ui._common.security;

import de.conet.isd.skima.skimabackend.api.ui._common.security.jwt.JwtTokenValidationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SkimaWebSecurityConfig {

    /**
     * List of Routes that will be accessible without authentication.
     */
    public static final String[] PUBLIC_URLS_WHITELIST = {
            "/api/login", "/api/_openapi/**", "/api/status", "/api/actuator/**",
    };

    private final JwtTokenValidationFilter jwtTokenValidationFilter;

    public SkimaWebSecurityConfig(JwtTokenValidationFilter jwtTokenValidationFilter) {
        this.jwtTokenValidationFilter = jwtTokenValidationFilter;
    }

    @Bean
    protected SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http
                // Settings for stateless backends for Single Page App:
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement((sessionManagement) ->
                        sessionManagement
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                // Use Custom JWT Auth:
                .addFilterBefore(jwtTokenValidationFilter, UsernamePasswordAuthenticationFilter.class)
                // Path Config:
                .authorizeHttpRequests(authReg -> authReg
                        // Whtielisted:
                        .requestMatchers(PUBLIC_URLS_WHITELIST).permitAll()
                        // Everything else must be authenticated:
                        .anyRequest().authenticated()

                )
                // Failed Login should result in 401, not 403
                .exceptionHandling((exceptionHandling) -> {
                    exceptionHandling.authenticationEntryPoint(
                            new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)
                    );
                });
        ;

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
