package de.conet.isd.skima.skimabackend.domain.entities.users;

import de.conet.isd.skima.skimabackend.domain.entities._common.PersistableEnum;
import de.conet.isd.skima.skimabackend.domain.entities._common.PersistableEnumConverter;
import de.conet.isd.skima.skimabackend.domain.entities._common.PersistableEnumSetConverter;
import jakarta.persistence.Converter;

public enum SkimaPermission implements PersistableEnum<String> {
    DEMO_POC(SkimaPermissionConstants.DEMO_POC),
    USER_MANAGEMENT(SkimaPermissionConstants.USER_MANAGEMENT),
    MY_SKILL_TOPICS(SkimaPermissionConstants.MY_SKILL_TOPICS),
    ;

    @Converter(autoApply = true)
    public static class EnumConverter extends PersistableEnumConverter<SkimaPermission, String> {
        protected Class<SkimaPermission> getEnumType() {
            return SkimaPermission.class;
        }
    }

    @Converter
    public static class EnumSetConverter extends PersistableEnumSetConverter<SkimaPermission, String> {
        protected Class<SkimaPermission> getEnumType() {
            return SkimaPermission.class;
        }
    }


    private final String dbId;

    SkimaPermission(String dbId) {
        this.dbId = dbId;
    }

    @Override
    public String getDbId() {
        return dbId;
    }

}
