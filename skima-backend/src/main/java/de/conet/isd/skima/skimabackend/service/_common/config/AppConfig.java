package de.conet.isd.skima.skimabackend.service._common.config;

import de.conet.isd.skima.skimabackend.service._common.error.SkimaConfigException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.ZonedDateTime;

/**
 * @param version For {@link #env()}=PREPROD or PROD, this is the semantic release version, e.g. "1.0.0";
 *                for local and dev, it's a hint or a buildnumber
 * @param versionTimestamp when this {@link #version()} was built
 * @param env see {@link SkimaEnvironment}
 * @param instance default instance 0; if multiple backend instances are deployed, they get assigned 1, 2, 3, ...
 */
@ConfigurationProperties("skima-backend.app")
public record AppConfig(String version, ZonedDateTime versionTimestamp, SkimaEnvironment env, int instance) {
    private static final Logger log = LoggerFactory.getLogger(AppConfig.class);

    public AppConfig {
        if (this.versionTimestamp() == null) versionTimestamp = ZonedDateTime.now();
    }
}
