package com.WilsonQdop.Chamadas;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@OpenAPIDefinition(info = @Info(description = "Api desenvolvida para controle de chamadas"))
public class ChamadasApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChamadasApplication.class, args);
	}

}
