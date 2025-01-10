package de.conet.isd.skima.skimabackend.domain.entities.skilltopics;


import de.conet.isd.skima.skimabackend.domain.entities._common.PersistableEnum;
import de.conet.isd.skima.skimabackend.domain.entities._common.PersistableEnumConverter;
import jakarta.persistence.Converter;

public enum ActivityMetric implements PersistableEnum<String> {
    CHAPTERS("CHAP", false),
    WEB_PAGES("WEB", true),
    TODOS("TODOS", true),
    CUSTOM("CUST", false),
    ;

    @Converter(autoApply = true)
    public static class EnumConverter extends PersistableEnumConverter<ActivityMetric, String> {
        protected Class<ActivityMetric> getEnumType() {
            return ActivityMetric.class;
        }
    }

    private final String dbId;

    /**
     * Activities with this metric type have a list of subtasks instead of a single "amount" value.
     */
    private final boolean requiresTaskLists;

    ActivityMetric(String dbId, boolean requiresTaskLists) {
        this.dbId = dbId;
        this.requiresTaskLists = requiresTaskLists;
    }

    @Override
    public String getDbId() {
        return dbId;
    }

    public boolean isRequiresTaskLists() {
        return requiresTaskLists;
    }
}
