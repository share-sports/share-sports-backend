package org.example.sharesportsbackend.auth.vo.in;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RefreshTokenRequestDto {
    private String refreshToken;

    @Builder
    public RefreshTokenRequestDto(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public static RefreshTokenRequestDto from(RefreshTokenRequestVo refreshTokenRequestVo) {
        return RefreshTokenRequestDto.builder()
                .refreshToken(refreshTokenRequestVo.getRefreshToken())
                .build();
    }
}
