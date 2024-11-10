package org.example.sharesportsbackend.reservation.application;


import lombok.RequiredArgsConstructor;
import org.example.sharesportsbackend.reservation.domain.Reservation;
import org.example.sharesportsbackend.reservation.domain.ReservationRequestDto;
import org.example.sharesportsbackend.reservation.domain.ReservationStatus;
import org.example.sharesportsbackend.reservation.dto.GetReservationListDto;
import org.example.sharesportsbackend.reservation.infrastructure.ReservationRepository;
import org.example.sharesportsbackend.stadium.domain.Stadium;
import org.example.sharesportsbackend.stadium.repository.StadiumRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final StadiumRepository stadiumRepository;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd:HH:mm");


    /**
     * 예약하기 (예약할 때 겹치는 시간이 있는지 확인해야 함.)
     */
    public void createReservation(ReservationRequestDto reservationRequestDto, String memberUuid) {
        // 1. 해당 구장에 예약된 시간 중 겹치는 예약이 있는지 확인
        boolean hasOverlap = reservationRepository.existsByStadiumAndTimeRange(reservationRequestDto.getStadiumUuid(), LocalDateTime.parse(reservationRequestDto.getStartTime(), FORMATTER), LocalDateTime.parse(reservationRequestDto.getEndTime(), FORMATTER));
        if (hasOverlap) {
            throw new IllegalStateException("이미 해당 시간에 예약이 존재합니다.");
        }

        reservationRepository.save(reservationRequestDto.createEntity(memberUuid));
    }

    /**
     * 예약 취소하기
     */
    public void cancelReservation(Long reservationId, String memberUuid) {
        // Reservation을 ID로 찾기
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("예약을 찾을 수 없습니다."));

        // 예약의 멤버 UUID가 요청한 멤버 UUID와 일치하는지 확인
        if (!reservation.getMemberUuid().equals(memberUuid)) {
            throw new IllegalStateException("예약을 취소할 권한이 없습니다.");
        }

        // 예약 상태를 CANCELED로 변경
        reservation.setStatus(ReservationStatus.CANCELED);

        // 변경된 예약 정보를 데이터베이스에 저장
        reservationRepository.save(reservation);
    }


    /**
     * 예약 목록 가져오기 (구장별 예약목록, 시간별 예약 목록, 사용자가 예약한 목록)
     */
    public List<GetReservationListDto> getReservationsByStadium(String stadiumUuid) {
        // Stadium 정보를 가져옴
        Stadium stadium = stadiumRepository.findByStadiumUuid(stadiumUuid)
                .orElseThrow(() -> new RuntimeException("Stadium not found for UUID: " + stadiumUuid));

        // StadiumUuid로 Reservation 목록을 가져옴
        List<Reservation> reservations = reservationRepository.findByStadiumUuid(stadiumUuid);

        // 각 Reservation에 대해 Stadium 정보를 추가로 조합하고 DTO로 변환
        return reservations.stream()
                .map(reservation -> GetReservationListDto.from(reservation, stadium))
                .collect(Collectors.toList());
    }



    public List<GetReservationListDto> getReservationsByUser(String memberUuid) {
        // Reservation 리스트를 가져옴
        List<Reservation> reservations = reservationRepository.findByMemberUuid(memberUuid);

        // 각 Reservation에 대해 Stadium 정보를 추가로 조회하고 DTO로 변환
        return reservations.stream()
                .map(reservation -> {
                    Stadium stadium = stadiumRepository.findByStadiumUuid(reservation.getStadiumUuid())
                            .orElseThrow(() -> new RuntimeException("Stadium not found for UUID: " + reservation.getStadiumUuid()));
                    return GetReservationListDto.from(reservation, stadium);
                })
                .collect(Collectors.toList());
    }

    public List<GetReservationListDto> getReservationsByStadiumAndDate(String stadiumUuid, String date) {
        // date 문자열의 공백과 따옴표 제거
        date = date.trim().replaceAll("^\"|\"$", "");

        // date 문자열을 LocalDate로 변환
        LocalDate targetDate = LocalDate.parse(date, DateTimeFormatter.ISO_DATE);

        // 해당 날짜의 시작 시간과 종료 시간 설정
        LocalDateTime startOfDay = targetDate.atStartOfDay();
        LocalDateTime endOfDay = targetDate.plusDays(1).atStartOfDay();

        // 예약 목록 조회
        List<Reservation> reservations = reservationRepository.findByStadiumUuidAndStartTimeBetween(
                stadiumUuid, startOfDay, endOfDay
        );

        // Reservation과 Stadium 객체를 사용하여 GetReservationListDto로 변환
        return reservations.stream()
                .map(reservation -> GetReservationListDto.from(reservation, stadiumRepository.findByStadiumUuid(stadiumUuid).get()))
                .collect(Collectors.toList());
    }
}
