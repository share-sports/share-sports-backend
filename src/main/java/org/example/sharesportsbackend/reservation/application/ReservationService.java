package org.example.sharesportsbackend.reservation.application;


import lombok.RequiredArgsConstructor;
import org.example.sharesportsbackend.member.domain.Member;
import org.example.sharesportsbackend.reservation.domain.Reservation;
import org.example.sharesportsbackend.reservation.infrastructure.ReservationRepository;
import org.example.sharesportsbackend.stadium.Stadium;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ReservationService {
    private final ReservationRepository reservationRepository;

    /**
     * 예약하기 (예약할 때 겹치는 시간이 있는지 확인해야 함.)
     */
    public Reservation createReservation(Member user, Stadium stadium, LocalDateTime startTime, LocalDateTime endTime, int playerCount) {
        // 1. 해당 구장에 예약된 시간 중 겹치는 예약이 있는지 확인
        boolean hasOverlap = reservationRepository.existsByStadiumAndTimeRange(stadium, startTime, endTime);
        if (hasOverlap) {
            throw new IllegalStateException("이미 해당 시간에 예약이 존재합니다.");
        }

        // 2. 예약 생성
        Reservation reservation = new Reservation(user, stadium, startTime, endTime, playerCount);
        return reservationRepository.save(reservation);
    }

    /**
     * 예약 취소하기
     */
    public void cancelReservation(Long reservationId, Member user) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("예약을 찾을 수 없습니다."));

        if (!reservation.getUser().equals(user)) {
            throw new IllegalStateException("예약을 취소할 권한이 없습니다.");
        }

        reservationRepository.delete(reservation);
    }


    /**
     * 예약 목록 가져오기 (구장별 예약목록, 시간별 예약 목록, 사용자가 예약한 목록)
     */
    public List<Reservation> getReservationsByStadium(Stadium stadium) {
        return reservationRepository.findByStadium(stadium);
    }

    public List<Reservation> getReservationsByTime(LocalDateTime startTime, LocalDateTime endTime) {
        return reservationRepository.findByReservationTimeBetween(startTime, endTime);
    }

    public List<Reservation> getReservationsByUser(Member user) {
        return reservationRepository.findByUser(user);
    }




}
