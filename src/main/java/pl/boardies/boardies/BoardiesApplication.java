package pl.boardies.boardies;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import pl.boardies.repositories.UserRepository;

@SpringBootApplication(scanBasePackages = { "pl.boardies"} )
@EntityScan("pl.boardies")
@EnableJpaRepositories(basePackageClasses = UserRepository.class)
public class BoardiesApplication {

	public static void main(String[] args) {
		SpringApplication.run(BoardiesApplication.class, args);
	}

}
