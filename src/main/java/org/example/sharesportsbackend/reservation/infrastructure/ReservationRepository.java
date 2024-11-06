package org.example.sharesportsbackend.reservation.infrastructure;

import org.example.sharesportsbackend.reservation.domain.Reservation;
import org.example.sharesportsbackend.stadium.Stadium;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation,Long> {
    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END FROM Reservation r WHERE r.stadium = :stadium AND ((r.startTime < :endTime AND r.endTime > :startTime))")
    boolean existsByStadiumAndTimeRange(@Param("stadium") Stadium stadium, @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    List<Reservation> findByStadium(Stadium stadium);

    @Query("SELECT r FROM Reservation r WHERE r.startTime BETWEEN :startTime AND :endTime OR r.endTime BETWEEN :startTime AND :endTime")
    List<Reservation> findByReservationTimeBetween(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

}
