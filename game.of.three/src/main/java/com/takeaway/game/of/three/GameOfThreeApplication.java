package com.takeaway.game.of.three;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
@EnableAutoConfiguration
public class GameOfThreeApplication {

	public static void main(String[] args) {
		SpringApplication.run(GameOfThreeApplication.class, args);
	}

}
