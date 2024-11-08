package org.example.sharesportsbackend.reservation.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ReservationRequestDto {
    private String memberUuid;
    private String stadiumUuid;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public Reservation createEntity(){
        return Reservation.builder()
                .memberUuid(memberUuid)
                .stadiumUuid(stadiumUuid)
                .startTime(startTime)
                .endTime(endTime)
                . status(ReservationStatus.ACCEPTED)
                .build();
    }
}
