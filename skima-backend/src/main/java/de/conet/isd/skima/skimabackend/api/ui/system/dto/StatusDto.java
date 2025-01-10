package de.conet.isd.skima.skimabackend.api.ui.system.dto;

import de.conet.isd.skima.skimabackend.service._common.config.SkimaEnvironment;

import java.time.ZonedDateTime;

/**
 * Contains Details about System Status
 *
 * @param env {@see AppConfig#env()}
 * @param version {@see AppConfig#version()}
 * @param versionTimestamp {@see AppConfig#versionTimestamp()}
 */
public record StatusDto(SkimaEnvironment env, String version, ZonedDateTime versionTimestamp) {

}
