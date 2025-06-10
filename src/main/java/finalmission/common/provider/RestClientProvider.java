package finalmission.common.provider;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import org.springframework.web.client.RestClient;

public class RestClientProvider {

    public static RestClient createRestClient(ApiProperties apiProperties) {
        String credentials = apiProperties.getSecretKey() + ":";
        String encoded = Base64.getEncoder().encodeToString(credentials.getBytes(StandardCharsets.UTF_8));

        return RestClient.builder()
                .baseUrl(apiProperties.getBaseUrl())
                .defaultHeader("Authorization", "Basic " + encoded)
                .defaultHeader("Content-Type", "application/json")
                .build();
    }
}
