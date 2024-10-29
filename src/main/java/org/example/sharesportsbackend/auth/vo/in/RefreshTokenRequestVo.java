package org.example.sharesportsbackend.auth.vo.in;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class RefreshTokenRequestVo {

    private String refreshToken;

    @Builder
    public RefreshTokenRequestVo(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
