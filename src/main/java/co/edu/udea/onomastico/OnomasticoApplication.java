package co.edu.udea.onomastico;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableJpaAuditing
public class OnomasticoApplication {

	public static void main(String[] args) {
		SpringApplication.run(OnomasticoApplication.class, args);
	}

}
