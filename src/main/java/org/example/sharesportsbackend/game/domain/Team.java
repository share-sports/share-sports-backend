package org.example.sharesportsbackend.game.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.sharesportsbackend.member.domain.Member;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // (A,B,C) or (Red, Green, Blue) 중에 하나
    private String teamName;

    @OneToMany(mappedBy = "team")
    private List<Player> players;

    @ManyToMany(mappedBy = "teams")
    private List<Game> games;

}
