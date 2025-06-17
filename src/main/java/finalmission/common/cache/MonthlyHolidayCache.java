package finalmission.common.cache;

import finalmission.client.HolidayClient;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MonthlyHolidayCache {

    private final HolidayClient holidayClient;
    private final AtomicReference<YearMonth> lastUpdatedMonth = new AtomicReference<>();
    private final AtomicReference<List<LocalDate>> cachedHolidays = new AtomicReference<>(Collections.emptyList());

    public MonthlyHolidayCache(HolidayClient holidayClient) {
        this.holidayClient = holidayClient;
    }

    @Scheduled(cron = "0 0 0 1 * *")
    public void refreshMonthlyHolidays() {
        String month = String.format("%02d", YearMonth.now().getMonthValue());
        List<LocalDate> holidays = holidayClient.getHolidays(YearMonth.now().getYear(), month);
        cachedHolidays.set(holidays);
        lastUpdatedMonth.set(YearMonth.now());
    }

    public List<LocalDate> getCachedHolidays() {
        return cachedHolidays.get();
    }
}
