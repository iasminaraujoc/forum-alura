package br.com.alura.forum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSpringDataWebSupport //modulo que suporta a paginação passando como parametro
@EnableCaching //modulo que suporta o cache
@EnableSwagger2
public class ForumApplication {
	//essa é a classe que usamos para rodar o projeto. Nao precisa adicionar TomCat, já automático
	public static void main(String[] args) {
		SpringApplication.run(ForumApplication.class, args);
	}

	//para rodar no profile dev, editar config do ForumApplication passando o parâmetro:
	//-Dspring.profiles.active=dev
	//lembrar de aplicar e dar ok
	//se não configurar, o padrão é default, que é rodar todas as classes

}
