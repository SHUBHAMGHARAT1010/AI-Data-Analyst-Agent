package com.aidataagent.ai_data_analyst;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AiDataAnalystApplication {

	public static void main(String[] args) {
		SpringApplication.run(AiDataAnalystApplication.class, args);
	}

}
