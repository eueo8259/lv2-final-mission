package finalmission.service;

import finalmission.common.exception.IncorrectInfo;
import finalmission.dto.reservation.ReservationRequest;
import finalmission.dto.reservation.ReservationResponse;
import finalmission.entity.Customer;
import finalmission.entity.Reservation;
import finalmission.common.exception.NotFountException;
import finalmission.repository.CustomerRepository;
import finalmission.repository.ReservationRepository;
import java.util.List;
import java.util.Objects;
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

    @Transactional
    public void deleteReservation(Long reservationId, Long loginId) {
        Optional<Reservation> reservation = reservationRepository.findById(reservationId);
        if (reservation.isEmpty()){
            throw new NotFountException("존재 하지 않는 예약입니다.");
        }
        if (!Objects.equals(reservation.get().getCustomer().getId(), loginId)){
            throw new IncorrectInfo("본인의 예약만 삭제할 수 있습니다.");
        }
        reservationRepository.deleteById(reservationId);
    }
}
