package org.example.sharesportsbackend.stadium.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Getter
public class Stadium {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stadium_id", nullable = false)
    private Long id;

    @Column(nullable = false, unique = true)
    private String stadiumUuid;

    @Column(nullable = false)
    private String hostUuid;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private boolean parking;

    @Column(nullable = false)
    private boolean shoeRent;

    @Column(nullable = false)
    private boolean ballRent;

    @Column(nullable = false)
    private boolean uniformRent;

    @Column(nullable = false)
    private int rentCost;

    @Column(nullable = false)
    private String openingHours;

    @Column(nullable = false)
    private String closingHours;

    @Column(nullable = false)
    private boolean deleted;

}
