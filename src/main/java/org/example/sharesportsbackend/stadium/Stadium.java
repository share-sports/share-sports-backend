package org.example.sharesportsbackend.stadium;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.sharesportsbackend.reservation.domain.Reservation;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Stadium {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String location;
    private Boolean isAvailable;

    @OneToMany(mappedBy = "stadium")
    private List<Reservation> reservations;
}
