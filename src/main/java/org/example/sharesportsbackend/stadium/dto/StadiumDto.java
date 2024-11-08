package org.example.sharesportsbackend.stadium.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.sharesportsbackend.stadium.domain.Stadium;

@Getter
@AllArgsConstructor
public class StadiumDto {
    private String stadiumUuid;
    private String name;
    private String address;
    private String phone;
    private String description;
    private boolean parking;
    private boolean shoeRent;
    private boolean ballRent;
    private boolean uniformRent;
    private int rentCost;
    private String openingHours;
    private String closingHours;

    // Stadium 엔티티를 StadiumDto로 변환하는 정적 팩토리 메서드
    public static StadiumDto fromEntity(Stadium stadium) {
        return new StadiumDto(
                stadium.getStadiumUuid(),
                stadium.getName(),
                stadium.getAddress(),
                stadium.getPhone(),
                stadium.getDescription(),
                stadium.isParking(),
                stadium.isShoeRent(),
                stadium.isBallRent(),
                stadium.isUniformRent(),
                stadium.getRentCost(),
                stadium.getOpeningHours(),
                stadium.getClosingHours()
        );
    }
}
