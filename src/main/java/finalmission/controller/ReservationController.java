package finalmission.controller;

import finalmission.common.annotation.LoginCustomerId;
import finalmission.dto.login.LoginInfo;
import finalmission.dto.reservation.ReservationRequest;
import finalmission.dto.reservation.ReservationResponse;
import finalmission.service.ReservationService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("reservations")
@AllArgsConstructor
public class ReservationController {
    private final ReservationService reservationService;

    @PostMapping
    public ReservationResponse create(
            @LoginCustomerId LoginInfo info,
            @RequestBody ReservationRequest request
    ) {
        return reservationService.saveReservation(request, info.id());
    }

    @GetMapping("mine")
    public List<ReservationResponse> getAll(@LoginCustomerId LoginInfo info) {
        return reservationService.findByCustomerId(info.id());
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable("id") Long id,
                       @LoginCustomerId LoginInfo info){
        reservationService.deleteReservation(id, info.id());
    }
}
