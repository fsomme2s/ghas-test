package de.conet.isd.skima.skimabackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class SkimaBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(SkimaBackendApplication.class, args);
	}

}
