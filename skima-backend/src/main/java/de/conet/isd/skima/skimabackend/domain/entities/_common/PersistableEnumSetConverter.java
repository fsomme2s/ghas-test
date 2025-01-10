package de.conet.isd.skima.skimabackend.domain.entities._common;

public abstract class PersistableEnumSetConverter<ENUM extends PersistableEnum<COL>, COL> extends AbstractPersistableEnumSetConverter<ENUM, COL> {

    protected COL getDbId(ENUM enumConstant) {
        return enumConstant.getDbId();
    }
}
