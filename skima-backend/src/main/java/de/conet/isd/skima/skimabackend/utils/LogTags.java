package de.conet.isd.skima.skimabackend.utils;

import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

/**
 * Strings to be used as Prefix for log messages, to make logs easy to filter.
 * <pre>{@code
 *  log.info("{}Suspicious user activity detected...", LogTags.SECURITY)
 *  log.info("{}My Action took >100ms", LogTags.PERF)
 *  => Becomes:
 *  "#SECURITY: Suspicious user activity detected"
 *  "#PERF: My Action took >100ms"
 *
 * }
 * </pre>
 */
public enum LogTags {
    /**
     * Logs by the UI sent to the backend.
     */
    UI(),

    /**
     * Logging special performance metrics.
     */
    PERF(),
    ;

    private final Marker marker;

    LogTags() {
        this.marker = MarkerFactory.getMarker(this.name());
    }

    @Override
    public String toString() {
        return "#" + super.toString() + ":";
    }

    public Marker marker() {
        return marker;
    }
}
