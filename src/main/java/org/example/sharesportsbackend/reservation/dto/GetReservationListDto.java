package org.example.sharesportsbackend.reservation.dto;

import lombok.Builder;
import lombok.Getter;
import org.example.sharesportsbackend.reservation.domain.Reservation;
import org.example.sharesportsbackend.reservation.domain.ReservationStatus;
import org.example.sharesportsbackend.stadium.domain.Stadium;

import java.time.LocalDateTime;


@Getter
@Builder
public class GetReservationListDto {

    private Long reservationId;
    private String memberUuid;
    private String memberName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    // Stadium 정보 추가
    private Long stadiumId;
    private String stadiumUuid;

    private String stadiumName;
    private String stadiumAddress;
    private String stadiumPhone;

    private ReservationStatus status;

    // Reservation과 Stadium을 받아 DTO로 변환하는 정적 메서드
    public static GetReservationListDto from(Reservation reservation, Stadium stadium, String memberName) {
        return GetReservationListDto.builder()
                .reservationId(reservation.getId())
                .memberUuid(reservation.getMemberUuid())
                .startTime(reservation.getStartTime())
                .endTime(reservation.getEndTime())
                .stadiumId(stadium.getStadiumId())
                .stadiumUuid(stadium.getStadiumUuid())
                .stadiumName(stadium.getStadiumName())
                .stadiumAddress(stadium.getAddress())
                .stadiumPhone(stadium.getPhone())
                .memberName(memberName)
                .build();
    }

    public static GetReservationListDto from(Reservation reservation, Stadium stadium) {
        return GetReservationListDto.builder()
                .reservationId(reservation.getId())
                .memberUuid(reservation.getMemberUuid())
                .startTime(reservation.getStartTime())
                .endTime(reservation.getEndTime())
                .stadiumId(stadium.getStadiumId())
                .stadiumUuid(stadium.getStadiumUuid())
                .stadiumName(stadium.getStadiumName())
                .stadiumAddress(stadium.getAddress())
                .stadiumPhone(stadium.getPhone())
                .build();
    }


}
