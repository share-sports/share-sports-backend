package org.example.sharesportsbackend.stadium.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.sharesportsbackend.global.common.response.BaseResponse;
import org.example.sharesportsbackend.stadium.application.StadiumService;
import org.example.sharesportsbackend.stadium.dto.StadiumDto;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "구장 관리 API", description = "구장 관련 API endpoints")
@RequestMapping("/api/stadium")
public class StadiumController {

    private final StadiumService stadiumService;

    @Operation(summary = "구장 목록 조회", description = "검색어에 따라 구장의 목록을 조회하는 API (검색어 없을 시 전체 목록 반환)")
    @GetMapping("/list")
    public BaseResponse<List<StadiumDto>> getStadiumList(@RequestParam(value = "inputText", required = false) String inputText) {
        // 검색어에 따라 구장을 조회하여 반환
        List<StadiumDto> stadiums = stadiumService.getStadiumList(inputText);
        return new BaseResponse<>(stadiums);
    }

    @Operation(summary = "구장 상세 정보 조회", description = "특정 구장의 상세 정보를 조회하는 API")
    @GetMapping("/{stadiumUuid}")
    public BaseResponse<StadiumDto> getStadiumDetails(@PathVariable String stadiumUuid) {
        // stadiumUuid에 해당하는 구장의 상세 정보를 StadiumDto 형태로 조회하여 반환
        StadiumDto stadium = stadiumService.getStadiumDetails(stadiumUuid);
        return new BaseResponse<>(stadium);
    }
}
