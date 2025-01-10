package de.conet.isd.skima.skimabackend.domain.entities._common;

import jakarta.persistence.AttributeConverter;
import java.util.Set;
import java.util.HashSet;

public abstract class AbstractPersistableEnumSetConverter<ENUM extends PersistableEnum<?>, COL>
        implements AttributeConverter<Set<ENUM>, Set<COL>> {

    protected abstract Class<ENUM> getEnumType();
    protected abstract COL getDbId(ENUM enumConstant);

    protected boolean strict() {
        return true;
    }

    @Override
    public Set<COL> convertToDatabaseColumn(Set<ENUM> enumSet) {
        if (enumSet == null || enumSet.isEmpty()) {
            return null;
        }
        Set<COL> dbIds = new HashSet<>();
        for (ENUM enumConstant : enumSet) {
            dbIds.add(getDbId(enumConstant));
        }
        return dbIds;
    }

    @Override
    public Set<ENUM> convertToEntityAttribute(Set<COL> dbEnumIds) {
        if (dbEnumIds == null || dbEnumIds.isEmpty()) {
            return new HashSet<>();
        }

        Set<ENUM> enumSet = new HashSet<>();
        for (COL dbEnumId : dbEnumIds) {
            boolean found = false;
            for (ENUM enumConst : getEnumType().getEnumConstants()) {
                if (getDbId(enumConst).equals(dbEnumId)) {
                    enumSet.add(enumConst);
                    found = true;
                    break;
                }
            }
            if (!found && strict()) {
                throw new IllegalArgumentException(
                        "No DB-ID '%s' found for Enum %s".formatted(dbEnumId, getEnumType().getName())
                );
            }
        }
        return enumSet;
    }
}
