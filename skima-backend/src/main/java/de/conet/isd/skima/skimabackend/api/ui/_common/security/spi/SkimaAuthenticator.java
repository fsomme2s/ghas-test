package de.conet.isd.skima.skimabackend.api.ui._common.security.spi;

public interface SkimaAuthenticator {
    AuthenticationResult authenticate(String username, String password);
}
