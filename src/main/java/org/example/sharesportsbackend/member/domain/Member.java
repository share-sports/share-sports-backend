package org.example.sharesportsbackend.member.domain;

import java.util.Date;
import java.util.List;

import jakarta.persistence.*;
import org.example.sharesportsbackend.auth.domain.Role;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.sharesportsbackend.reservation.domain.Reservation;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)

public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String memberUuid;

    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;

    private Date birth;


    @Builder
    public Member(Long id, String memberUuid, String email, Role role, String name, String password, Date birth) {
        this.id = id;
        this.memberUuid = memberUuid;
        this.email = email;
        this.role = role;
        this.name = name;
        this.password = password;
        this.birth = birth;
    }
}
