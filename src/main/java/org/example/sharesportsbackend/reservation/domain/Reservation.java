package org.example.sharesportsbackend.reservation.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.sharesportsbackend.game.domain.Team;
import org.example.sharesportsbackend.member.domain.Member;
import org.example.sharesportsbackend.stadium.Stadium;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Member user;

    @ManyToOne
    @JoinColumn(name = "stadium_id")
    private Stadium stadium;

    private LocalDateTime reservationTime;
    private Boolean isMatched;
    private int currentPlayerCount;
    private int maxPlayerCount;

    @ManyToMany
    @JoinTable(
            name = "reservation_team",
            joinColumns = @JoinColumn(name = "reservation_id"),
            inverseJoinColumns = @JoinColumn(name = "team_id")
    )
    private List<Team> teams;


}
