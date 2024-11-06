package org.example.sharesportsbackend.stadium.repository;

import org.example.sharesportsbackend.stadium.domain.Stadium;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface StadiumRepository extends JpaRepository<Stadium,Long> {
    Optional<Stadium> findByStadiumUuid(String stadiumUuid);
    // 이름에 검색어가 포함된 구장 목록을 조회하는 메서드
    List<Stadium> findByNameContaining(String name);

}
