package org.example.sharesportsbackend.game.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Round {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id")
    private Game game;

    private int roundNumber;

    @ManyToOne
    @JoinColumn(name = "winning_team_id")
    private Team winningTeam;

    @ManyToOne
    @JoinColumn(name = "losing_team_id")
    private Team losingTeam;


    private Integer winningTeamScore;

    private Integer losingTeamScore;

    @OneToMany(mappedBy = "round")
    private List<PlayerRecord> playerRecords;

}
