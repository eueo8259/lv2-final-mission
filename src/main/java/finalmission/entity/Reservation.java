package finalmission.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    Customer customer;

    @Column(nullable = false)
    LocalDate date;

    @Column(nullable = false)
    LocalTime time;

    public Reservation(Customer customer, LocalDate date, LocalTime time) {
        this.customer = customer;
        this.date = date;
        this.time = time;
    }
}
