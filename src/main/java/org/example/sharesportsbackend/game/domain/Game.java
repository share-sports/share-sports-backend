package org.example.sharesportsbackend.game.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany
    @JoinTable(
            name = "game_team",
            joinColumns = @JoinColumn(name = "game_id"),
            inverseJoinColumns = @JoinColumn(name = "team_id")
    )
    private List<Team> teams;

    private LocalDateTime gameTime;

    @ManyToOne
    @JoinColumn(name = "mvp_player_id")
    private Player mvpPlayer;

    @ManyToOne
    @JoinColumn(name = "fair_play_player_id")
    private Player fairPlayPlayer;

    @OneToMany(mappedBy = "game")
    private List<PlayerRecord> playerRecords;

    @OneToMany(mappedBy = "game")
    private List<Round> rounds;


}
