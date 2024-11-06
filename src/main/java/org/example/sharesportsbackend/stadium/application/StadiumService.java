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
     * 모든 구장의 목록 조회
     */
    public List<StadiumDto> getAllStadiums() {
        return stadiumRepository.findAll().stream()
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
