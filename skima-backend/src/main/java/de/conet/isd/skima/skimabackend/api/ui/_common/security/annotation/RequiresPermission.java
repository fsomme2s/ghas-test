package de.conet.isd.skima.skimabackend.api.ui._common.security.annotation;

import de.conet.isd.skima.skimabackend.domain.entities.users.SkimaPermission;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequiresPermission {
    SkimaPermission value();
}
