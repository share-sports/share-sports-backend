package org.example.sharesportsbackend.reservation.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.sharesportsbackend.game.domain.Team;
import org.example.sharesportsbackend.member.domain.Member;
import org.example.sharesportsbackend.stadium.Stadium;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DialectOverride;
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

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private Boolean isMatched;


    private int currentPlayerCount = 0;

    @ManyToMany
    @JoinTable(
            name = "reservation_team",
            joinColumns = @JoinColumn(name = "reservation_id"),
            inverseJoinColumns = @JoinColumn(name = "team_id")
    )
    private List<Team> teams;

    public Reservation(Member user, Stadium stadium, LocalDateTime startTime, LocalDateTime endTime, int playerCount) {
        this.user = user;
        this.stadium = stadium;
        this.startTime = startTime;
        this.endTime = endTime;
        this.isMatched = false;
        this.currentPlayerCount += playerCount;
    }
}
