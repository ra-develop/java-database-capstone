package com.project.back_end;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication
@EnableJpaRepositories("com.project.back_end.repo")
@EntityScan("com.project.back_end.models")
@ComponentScan("com.project.back_end")
public class BackEndApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackEndApplication.class, args);
	}

}
