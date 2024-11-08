package org.example.sharesportsbackend.reservation.domain;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ReservationRequestDto {
    private String stadiumUuid;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public Reservation createEntity(String memberUuid){
        return Reservation.builder()
                .memberUuid(memberUuid)
                .stadiumUuid(stadiumUuid)
                .startTime(startTime)
                .endTime(endTime)
                . status(ReservationStatus.ACCEPTED)
                .build();
    }

}
