package br.com.alura.forum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ForumApplication {
	//essa é a classe que usamos para rodar o projeto. Nao precisa adicionar TomCat, já automático
	public static void main(String[] args) {
		SpringApplication.run(ForumApplication.class, args);
	}

}
