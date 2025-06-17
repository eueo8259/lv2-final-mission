package finalmission.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class HolidayClient {

    private final String holidayApiUrl;
    private final String serviceKey;
    private final ObjectMapper objectMapper;
    private final RestClient restClient;

    public HolidayClient(
            @Value("${external.holiday.baseUrl}") String holidayApiUrl,
            @Value("${external.holiday.secretKey}") String serviceKey,
            ObjectMapper objectMapper,
            RestClient restClient
    ) {
        this.holidayApiUrl = holidayApiUrl;
        this.serviceKey = serviceKey;
        this.objectMapper = objectMapper;
        this.restClient = restClient;
    }

    public List<LocalDate> getHolidays(int year, String month) {
        try {
            URI uri = buildRequestUri(year, month);
            String responseBody = fetchResponse(uri);
            return extractHolidayDates(responseBody);
        } catch (JsonProcessingException | URISyntaxException e) {
            throw new IllegalStateException("공휴일 정보를 가져오는 중 오류 발생: " + e.getMessage(), e);
        }
    }

    private URI buildRequestUri(int year, String month) throws URISyntaxException {
        String url = String.format("%s?serviceKey=%s&solYear=%d&solMonth=%s&_type=json",
                holidayApiUrl, serviceKey, year, month);
        return new URI(url);
    }

    private String fetchResponse(URI uri) {
        return restClient.get()
                .uri(uri)
                .retrieve()
                .body(String.class);
    }

    private List<LocalDate> extractHolidayDates(String responseBody) throws JsonProcessingException {
        JsonNode bodyNode = objectMapper.readTree(responseBody)
                .path("response")
                .path("body");

        if (isNoHoliday(bodyNode)) {
            return Collections.emptyList();
        }

        return parseHolidayItems(bodyNode.path("items").path("item"));
    }

    private boolean isNoHoliday(JsonNode bodyNode) {
        return bodyNode.path("totalCount").asInt() == 0;
    }

    private List<LocalDate> parseHolidayItems(JsonNode itemNodes) {
        List<LocalDate> holidays = new ArrayList<>();
        for (JsonNode itemNode : itemNodes) {
            holidays.add(convertLocdateToDate(itemNode));
        }
        return holidays;
    }

    private LocalDate convertLocdateToDate(JsonNode itemNode) {
        String locdate = itemNode.path("locdate").asText();
        return parseLocDate(locdate);
    }

    private LocalDate parseLocDate(String locdate) {
        return LocalDate.of(
                Integer.parseInt(locdate.substring(0, 4)),
                Integer.parseInt(locdate.substring(4, 6)),
                Integer.parseInt(locdate.substring(6, 8))
        );
    }
}
