package br.com.alura.forum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

@SpringBootApplication
@EnableSpringDataWebSupport //modulo que suporta a paginação passando como parametro
public class ForumApplication {
	//essa é a classe que usamos para rodar o projeto. Nao precisa adicionar TomCat, já automático
	public static void main(String[] args) {
		SpringApplication.run(ForumApplication.class, args);
	}

}
