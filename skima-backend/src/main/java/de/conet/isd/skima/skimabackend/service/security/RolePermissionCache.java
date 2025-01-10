package de.conet.isd.skima.skimabackend.service.security;

import de.conet.isd.skima.skimabackend.domain.entities.users.SkimaPermission;
import de.conet.isd.skima.skimabackend.domain.entities.users.SkimaRole;
import de.conet.isd.skima.skimabackend.service.users.SkimaRoleRepo;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RolePermissionCache {

    private final SkimaRoleRepo skimaRoleRepo;
    private Map<String, Set<SkimaPermission>> cache;

    public RolePermissionCache(SkimaRoleRepo skimaRoleRepo) {
        this.skimaRoleRepo = skimaRoleRepo;
    }

    public Set<SkimaPermission> get(String key) {
        Set<SkimaPermission> result = getCache().get(key);
        return result != null ? result : new HashSet<>();
    }

    public Map<String, Set<SkimaPermission>> getCache() {
        if (cache == null) {
            List<SkimaRole> allRoles = skimaRoleRepo.findAll();
            this.cache = allRoles.stream().collect(Collectors.toMap(SkimaRole::getName, SkimaRole::getPermissions));
        }

        return cache;
    }

}
