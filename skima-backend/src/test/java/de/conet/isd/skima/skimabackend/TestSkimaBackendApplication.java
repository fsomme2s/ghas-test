package de.conet.isd.skima.skimabackend;

import org.springframework.boot.SpringApplication;

public class TestSkimaBackendApplication {

	public static void main(String[] args) {
		SpringApplication.from(SkimaBackendApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
