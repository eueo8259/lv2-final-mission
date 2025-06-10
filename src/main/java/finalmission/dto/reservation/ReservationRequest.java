package finalmission.dto.reservation;

import java.time.LocalDate;
import java.time.LocalTime;

public record ReservationRequest(
        LocalDate date,
        LocalTime time
) {

}
