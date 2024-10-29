package org.example.sharesportsbackend.auth.vo.out;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class JwtTokenResponseVo {

    private String grantType;
    private String accessToken;
    private String refreshToken;
    private String memberUuid;
    private String name;

    @Builder
    public JwtTokenResponseVo(String grantType, String accessToken, String refreshToken, String memberUuid, String name) {
        this.grantType = grantType;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.memberUuid = memberUuid;
        this.name = name;
    }
}
