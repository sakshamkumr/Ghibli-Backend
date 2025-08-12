package com.Ghibli.GhibliAPI;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class GhibliApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(GhibliApiApplication.class, args);
	}

}
