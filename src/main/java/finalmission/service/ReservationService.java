package finalmission.service;

import finalmission.dto.reservation.ReservationRequest;
import finalmission.dto.reservation.ReservationResponse;
import finalmission.entity.Customer;
import finalmission.entity.Reservation;
import finalmission.exception.NotFountException;
import finalmission.repository.CustomerRepository;
import finalmission.repository.ReservationRepository;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final CustomerRepository customerRepository;

    public List<ReservationResponse> findByCustomerId(Long id) {
        List<Reservation> findReservation = reservationRepository.findByCustomerId(id);
        return findReservation.stream()
                .map(ReservationResponse::of)
                .toList();
    }

    @Transactional
    public ReservationResponse saveReservation(ReservationRequest request, Long id) {
        Optional<Customer> customer = customerRepository.findById(id);
        if (customer.isEmpty()) {
            throw new NotFountException("존재 하지 않는 고객입니다.");
        }
        Reservation saveReservation = reservationRepository.save(
                new Reservation(customer.get(), request.date(), request.time())
        );
        return ReservationResponse.of(saveReservation);
    }
}
