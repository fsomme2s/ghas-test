package de.conet.isd.skima.skimabackend.service._common.error;

public class SkimaConfigException extends RuntimeException {
    public SkimaConfigException(String message) {
        super(message);
    }

    public SkimaConfigException(String message, Throwable cause) {
        super(message, cause);
    }
}
