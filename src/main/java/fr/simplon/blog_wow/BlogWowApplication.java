package fr.simplon.blog_wow;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BlogWowApplication {

	public static void main(String[] args) {
		SpringApplication.run(BlogWowApplication.class, args);
	}

}

@OpenAPIDefinition(info = @Info(title = "Open API specifications",
		description = "Points d'entrée de l'API de gestion des articles"),
		servers = @Server(url = "http://localhost:8081/api"))
class OpenApiConfiguration
{
}
