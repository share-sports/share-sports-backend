package org.example.sharesportsbackend.reservation.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.sharesportsbackend.global.common.response.BaseResponse;
import org.example.sharesportsbackend.reservation.application.ReservationService;
import org.example.sharesportsbackend.reservation.domain.ReservationRequestDto;
import org.example.sharesportsbackend.reservation.dto.GetReservationListDto;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "예약 관리 API", description = "예약 관련 API endpoints")
@RequestMapping("/api/reservation/")
public class ReservationController {

    private final ReservationService reservationService;

    @Operation(summary = "내 예약 조회", description = "내가 한 예약 조회 API")
    @GetMapping("/my")
    public BaseResponse<List<GetReservationListDto>> getReservationList(@AuthenticationPrincipal UserDetails userDetails) {
        // 현재 인증된 사용자의 username (memberUuid)를 이용하여 예약 목록을 조회
        String memberUuid = userDetails.getUsername();
        List<GetReservationListDto> list = reservationService.getReservationsByUser(memberUuid);

        // BaseResponse로 감싸서 반환
        return new BaseResponse<>(list);
    }

    @Operation(summary = "구장별 예약 조회", description = "특정 스타디움의 예약 목록을 조회하는 API")
    @GetMapping("/stadium/{stadiumUuid}")
    public BaseResponse<List<GetReservationListDto>> getReservationsByStadium(@PathVariable String stadiumUuid) {
        // 스타디움 UUID를 통해 예약 목록 조회
        List<GetReservationListDto> list = reservationService.getReservationsByStadium(stadiumUuid);

        // BaseResponse로 감싸서 반환
        return new BaseResponse<>(list);
    }


    @Operation(summary = "예약하기", description = "예약 추가 API")
    @PostMapping("/reserve")
    public BaseResponse<Void> createReservation(@AuthenticationPrincipal UserDetails userDetails, ReservationRequestDto reservationRequestDto){
        reservationService.createReservation(reservationRequestDto);
        return new BaseResponse<>();
    }


    @Operation(summary = "예약 취소하기", description = "예약 삭제 API")
    @PostMapping("/cancel")
    public BaseResponse<Void> deleteReservation(@AuthenticationPrincipal UserDetails userDetails, Long reservationId){
        reservationService.cancelReservation(reservationId,userDetails.getUsername());
        return new BaseResponse<>();
    }





}
