package de.conet.isd.skima.skimabackend.domain.entities._common;

public abstract class PersistableEnumConverter<ENUM extends PersistableEnum<COL>, COL> extends AbstractPersistableEnumConverter<ENUM, COL> {

    protected COL getDbId(ENUM enumConstant) {
        return enumConstant.getDbId();
    }
}
