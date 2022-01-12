package yangbrothers.movierank.config;

import kr.or.kobis.kobisopenapi.consumer.rest.KobisOpenAPIRestService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MovieApiConfig {
    @Value("${api.key}")
    private String key;

    @Bean
    public KobisOpenAPIRestService kobisOpenAPIRestService() {
        return new KobisOpenAPIRestService(key);
    }
}
