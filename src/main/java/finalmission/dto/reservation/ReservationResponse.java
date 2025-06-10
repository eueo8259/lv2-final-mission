package finalmission.dto.reservation;

import finalmission.entity.Reservation;
import java.time.LocalDate;
import java.time.LocalTime;

public record ReservationResponse(
        LocalDate date,
        LocalTime time
) {
    public static ReservationResponse of(Reservation reservation) {
        return new ReservationResponse(
                reservation.getDate(),
                reservation.getTime()
        );
    }
}
