package org.example.sharesportsbackend.stadium.repository;

import org.example.sharesportsbackend.stadium.domain.Stadium;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface StadiumRepository extends JpaRepository<Stadium,Long> {
    Optional<Stadium> findByStadiumUuid(String stadiumUuid);

}
