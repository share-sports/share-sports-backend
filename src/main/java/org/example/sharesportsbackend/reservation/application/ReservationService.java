package org.example.sharesportsbackend.reservation.application;


import lombok.RequiredArgsConstructor;
import org.example.sharesportsbackend.reservation.domain.Reservation;
import org.example.sharesportsbackend.reservation.domain.ReservationRequestDto;
import org.example.sharesportsbackend.reservation.dto.GetReservationListDto;
import org.example.sharesportsbackend.reservation.infrastructure.ReservationRepository;
import org.example.sharesportsbackend.stadium.domain.Stadium;
import org.example.sharesportsbackend.stadium.repository.StadiumRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final StadiumRepository stadiumRepository;

    /**
     * 예약하기 (예약할 때 겹치는 시간이 있는지 확인해야 함.)
     */
    public void createReservation(ReservationRequestDto reservationRequestDto) {
        // 1. 해당 구장에 예약된 시간 중 겹치는 예약이 있는지 확인
        boolean hasOverlap = reservationRepository.existsByStadiumAndTimeRange(reservationRequestDto.getStadiumUuid(), reservationRequestDto.getStartTime(), reservationRequestDto.getEndTime());
        if (hasOverlap) {
            throw new IllegalStateException("이미 해당 시간에 예약이 존재합니다.");
        }

        reservationRepository.save(reservationRequestDto.createEntity());
    }

    /**
     * 예약 취소하기
     */
    public void cancelReservation(Long reservationId, String memberUuid) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("예약을 찾을 수 없습니다."));

        if (!reservation.getMemberUuid().equals(memberUuid)) {
            throw new IllegalStateException("예약을 취소할 권한이 없습니다.");
        }
        reservationRepository.delete(reservation);
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

}
