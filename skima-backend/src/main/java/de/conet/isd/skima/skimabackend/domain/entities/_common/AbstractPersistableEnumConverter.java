package de.conet.isd.skima.skimabackend.domain.entities._common;

import jakarta.persistence.AttributeConverter;

public abstract class AbstractPersistableEnumConverter<ENUM extends PersistableEnum<?>, COL> implements AttributeConverter<ENUM, COL> {

    protected abstract Class<ENUM> getEnumType();
    protected abstract COL getDbId(ENUM enumConstant);

    /**
     * @return true = {@link #convertToEntityAttribute(Object)} only accepts DB-IDs, that an ENUM exists for (DEFAULT).
     *  false = returns null for unknown ids found in database
     */
    protected boolean strict() {
        return true;
    }


    @Override
    public COL convertToDatabaseColumn(ENUM enumConstant) {
        return enumConstant != null ? getDbId(enumConstant) : null;
    }


    @Override
    public ENUM convertToEntityAttribute(COL dbEnumId) {
        if (dbEnumId == null) {
            return null;
        }

        for (ENUM enumConst : getEnumType().getEnumConstants()){
            if (getDbId(enumConst).equals(dbEnumId)) {
                return enumConst;
            }
        }

        if (strict()) {
            throw new IllegalArgumentException(
                    "No DB-ID '%s' found for Enum %s".formatted(dbEnumId, getEnumType().getName())
            );
        } else {
            return null;
        }
    }
}
