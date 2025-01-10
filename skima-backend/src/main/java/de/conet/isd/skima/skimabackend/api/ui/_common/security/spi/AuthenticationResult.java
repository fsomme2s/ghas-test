package de.conet.isd.skima.skimabackend.api.ui._common.security.spi;

import java.util.HashSet;
import java.util.Set;

public record AuthenticationResult(boolean authenticated, Set<String> roles) {

    public AuthenticationResult(boolean authenticated) {
        this(authenticated, null);
    }

    public AuthenticationResult(boolean authenticated, Set<String> roles) {
        this.authenticated = authenticated;
        this.roles = roles != null ? new HashSet<>(roles) : null;
    }
}
