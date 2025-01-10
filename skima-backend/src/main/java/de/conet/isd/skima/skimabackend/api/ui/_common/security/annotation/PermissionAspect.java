package de.conet.isd.skima.skimabackend.api.ui._common.security.annotation;

import de.conet.isd.skima.skimabackend.domain.entities.users.SkimaPermission;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.JoinPoint;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class PermissionAspect {

    @Before("@annotation(requiresPermission)")
    public void checkPermission(JoinPoint joinPoint, RequiresPermission requiresPermission) {
        SkimaPermission requiredPermission = requiresPermission.value();
        String requiredAuthority = requiredPermission.getDbId();

        boolean hasPermission = SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(requiredAuthority::equals);

        if (!hasPermission) {
            throw new AccessDeniedException("Access is denied");
        }
    }
}
