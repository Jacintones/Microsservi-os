package com.livraria.jacintoslibrary;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class JacintosLibraryApplication {

	public static void main(String[] args) {
		SpringApplication.run(JacintosLibraryApplication.class, args);
	}

}
