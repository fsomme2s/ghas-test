package de.conet.isd.skima.skimabackend.adapters.login;

import de.conet.isd.skima.skimabackend.api.ui._common.security.spi.AuthenticationResult;
import de.conet.isd.skima.skimabackend.api.ui._common.security.spi.SkimaAuthenticator;
import de.conet.isd.skima.skimabackend.service._common.config.SpringPofiles;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Set;

@Component
@Profile(SpringPofiles.LOCAL)
public class LocalMockSkimaAuthenticator implements SkimaAuthenticator {

    /**
     * Password that can be used for every user on LOCAL environment.
     */
    public static final String MOCK_PASSWORD = "s";

    @Override
    public AuthenticationResult authenticate(String username, String password) {
        return MOCK_PASSWORD.equals(password)
                ?  new AuthenticationResult(true, Set.of(Objects.equals(username, "admin") ? "TECH_ADMIN" : "BASIC"))
                :  new AuthenticationResult(false);
    }
}
