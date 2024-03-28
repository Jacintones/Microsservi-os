package br.thiago.autenticacao;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class AutenticacaoApplication {

	public static void main(String[] args) {
		SpringApplication.run(AutenticacaoApplication.class, args);
	}

}
