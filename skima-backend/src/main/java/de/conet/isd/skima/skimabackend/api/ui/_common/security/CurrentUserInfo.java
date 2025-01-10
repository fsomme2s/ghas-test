package de.conet.isd.skima.skimabackend.api.ui._common.security;

import de.conet.isd.skima.skimabackend.domain.entities.users.SkimaPermission;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import java.util.List;
import java.util.Set;

/**
 * Infos on the user who is authenticated for the current request.
 * They are taken or calculated from the JWT Claims.
 */
public class CurrentUserInfo extends PreAuthenticatedAuthenticationToken {

    private final long id;
    private final Set<String> roles;
    private final Set<SkimaPermission> permissions;

    public CurrentUserInfo(String principal, String token, long id, Set<String> roles, Set<SkimaPermission> permissions) {
        super(principal, token, toAuthorities(permissions));
        this.roles = roles;
        this.permissions = permissions;
        setAuthenticated(true);

        this.id = id;
    }

    private static List<SimpleGrantedAuthority> toAuthorities(Set<SkimaPermission> permissions) {
        return permissions.stream().map(perm -> new SimpleGrantedAuthority(perm.getDbId())).toList();
    }

    public long getId() {
        return id;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public Set<SkimaPermission> getPermissions() {
        return permissions;
    }

    @Override
    public String toString() {
        return "%s (%d)".formatted(getName(), getId());
    }

}
