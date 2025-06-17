package finalmission.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class HolidayClientConfig {

    @Bean
    public RestClient restClient() {
        return RestClient.builder().build();
    }
}
