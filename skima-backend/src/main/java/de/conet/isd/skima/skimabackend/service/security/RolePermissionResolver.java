package de.conet.isd.skima.skimabackend.service.security;

import de.conet.isd.skima.skimabackend.domain.entities.users.SkimaPermission;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RolePermissionResolver {

    private final RolePermissionCache rolePermissionCache;

    public RolePermissionResolver(RolePermissionCache rolePermissionCache) {
        this.rolePermissionCache = rolePermissionCache;
    }

    public Set<SkimaPermission> resolve(String role) {
        return rolePermissionCache.get(role);
    }

    public Set<SkimaPermission> resolve(Collection<String> rolles) {
        return rolles.stream().flatMap(r-> resolve(r).stream()).collect(Collectors.toSet());
    }
}
