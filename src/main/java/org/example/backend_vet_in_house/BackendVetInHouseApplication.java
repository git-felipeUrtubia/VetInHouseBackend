package org.example.backend_vet_in_house;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BackendVetInHouseApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendVetInHouseApplication.class, args);
	}

}
