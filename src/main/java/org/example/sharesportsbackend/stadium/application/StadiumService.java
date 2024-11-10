package org.example.sharesportsbackend.stadium.application;

import lombok.RequiredArgsConstructor;
import org.example.sharesportsbackend.stadium.domain.Stadium;
import org.example.sharesportsbackend.stadium.dto.StadiumDto;
import org.example.sharesportsbackend.stadium.repository.StadiumRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class StadiumService {
    private final StadiumRepository stadiumRepository;

    /***
     * 구장 목록 검색 및 조회
     * @param inputText 검색어 (구장 이름)
     * @return StadiumDto 리스트
     */
    public List<StadiumDto> getStadiumList(String inputText) {
        // 입력값이 있으면 해당 이름이 포함된 구장을 검색하고, 없으면 전체 구장을 조회
        List<Stadium> stadiums;
        if (inputText == null || inputText.isEmpty()) {
            stadiums = stadiumRepository.findAll();
        } else {
            stadiums = stadiumRepository.findByStadiumNameContaining(inputText);
        }

        // StadiumDto로 변환하여 반환
        return stadiums.stream()
                .map(StadiumDto::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * 특정 구장의 상세 정보 조회
     */
    public StadiumDto getStadiumDetails(String stadiumUuid) {
        Stadium stadium = stadiumRepository.findByStadiumUuid(stadiumUuid)
                .orElseThrow(() -> new RuntimeException("Stadium not found for UUID: " + stadiumUuid));
        return StadiumDto.fromEntity(stadium);
    }
}
