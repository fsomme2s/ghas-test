package de.conet.isd.skima.skimabackend.domain.entities.users;

import de.conet.isd.skima.skimabackend.domain.entities._common.AbstractEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.HashSet;
import java.util.Set;

@Entity
public class SkimaRole extends AbstractEntity {

    @Column(nullable = false, unique = true)
    private String name;

    @JdbcTypeCode(SqlTypes.JSON)
    @Convert(converter = SkimaPermission.EnumSetConverter.class)
    private Set<SkimaPermission> permissions = new HashSet<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @SuppressWarnings("JpaAttributeTypeInspection")
    public Set<SkimaPermission> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<SkimaPermission> permissions) {
        this.permissions = permissions;
    }
}
