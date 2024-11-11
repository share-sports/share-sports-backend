package org.example.sharesportsbackend.reservation.domain;

import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ReservationRequestDto {
    private String stadiumUuid;
    private String startTime;
    private String endTime;

    // DateTimeFormatter로 yyyy-MM-dd:HH:mm 형식에 맞춘 포맷터 생성
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd:HH:mm");

    // LocalDateTime 변환 메서드
    private LocalDateTime parseDateTime(String dateTime) {
        return LocalDateTime.parse(dateTime, FORMATTER);
    }

    public Reservation createEntity(String memberUuid) {
        return Reservation.builder()
                .memberUuid(memberUuid)
                .stadiumUuid(stadiumUuid)
                .startTime(parseDateTime(startTime)) // String을 LocalDateTime으로 변환
                .endTime(parseDateTime(endTime))     // String을 LocalDateTime으로 변환
                .status(ReservationStatus.ACCEPTED)
                .build();
    }
}
