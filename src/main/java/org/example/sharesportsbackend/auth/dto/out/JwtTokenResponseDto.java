package org.example.sharesportsbackend.auth.dto.out;

import org.example.sharesportsbackend.auth.vo.out.JwtTokenResponseVo;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class JwtTokenResponseDto {
    private String grantType;
    private String accessToken;
    private String refreshToken;
    private String memberUuid;
    private String name;

    @Builder
    public JwtTokenResponseDto(String grantType, String accessToken, String refreshToken, String memberUuid, String name) {
        this.grantType = grantType;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.memberUuid = memberUuid;
        this.name = name;
    }

    public JwtTokenResponseVo toVo() {
        return JwtTokenResponseVo.builder()
                .grantType(grantType)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .memberUuid(memberUuid)
                .name(name)
                .build();
    }
}
