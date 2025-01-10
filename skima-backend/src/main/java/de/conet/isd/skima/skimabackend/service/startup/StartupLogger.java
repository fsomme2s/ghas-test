package de.conet.isd.skima.skimabackend.service.startup;

import de.conet.isd.skima.skimabackend.domain.entities.users.SkimaPermission;
import de.conet.isd.skima.skimabackend.domain.entities.users.SkimaRole;
import de.conet.isd.skima.skimabackend.service._common.config.AppConfig;
import de.conet.isd.skima.skimabackend.service.users.SkimaRoleRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class StartupLogger implements ApplicationRunner {
    private final Logger log = LoggerFactory.getLogger(StartupLogger.class);

    private final AppConfig appConfig;
    private final SkimaRoleRepo skimaRoleRepo;

    @Value("${server.port:8080}")
    private int port;

    @Value("${springdoc.swagger-ui.path}")
    private String swaggerUiPath;

    public StartupLogger(AppConfig appConfig, SkimaRoleRepo skimaRoleRepo) {
        this.appConfig = appConfig;
        this.skimaRoleRepo = skimaRoleRepo;
    }

    @Override
    public void run(ApplicationArguments args) {
        log.info("");
        log.info("+++++++++++++++++++++++++");
        log.info("Starting Skill Manager Backend Instance {} in Env {} on port {}...",
                appConfig.instance(), appConfig.env(), port);
        log.info("Try the API on http://localhost:{}{}", port, swaggerUiPath);
        log.info("+++++++++++++++++++++++++");
    }
}
