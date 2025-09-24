package org.mrbag.AdminSpringBoot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import de.codecentric.boot.admin.server.config.EnableAdminServer;

@EnableAdminServer
@EnableWebSecurity
@SpringBootApplication
public class AdminSpringBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(AdminSpringBootApplication.class, args);
	}

}
