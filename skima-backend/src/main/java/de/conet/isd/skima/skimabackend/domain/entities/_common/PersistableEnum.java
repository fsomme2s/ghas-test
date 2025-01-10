package de.conet.isd.skima.skimabackend.domain.entities._common;

public interface PersistableEnum<COL> {
    COL getDbId();
    String name();
}
