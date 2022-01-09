package yangbrothers.movierank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
public class MovierankApplication {

    public static void main(String[] args) {

        SpringApplication.run(MovierankApplication.class, args);

    }
}