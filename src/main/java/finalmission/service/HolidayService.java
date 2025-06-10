package finalmission.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import finalmission.dto.HolidayInfo;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class HolidayService {
    private  RestClient restClient;
    private ObjectMapper objectMapper;
    private static final int YEAR = LocalDate.now().getYear();
    private static final int MONTH = LocalDate.now().getMonthValue();

    @Value("${external.holiday.secretKey}")
    private String holidayURL;

    @Value("${external.holiday.secretKey}")
    private String serviceKey;

    public List<HolidayInfo> getMonthHolidayInfo() {
        List<HolidayInfo> items = new ArrayList<>();

        return restClient.get()
                .uri(holidayURL + "?serviceKey=" + serviceKey + "&solYear=" + YEAR + "&solMonth=" + MONTH + "&_type=json")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body();
    }
}
