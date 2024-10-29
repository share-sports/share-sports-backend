package org.example.sharesportsbackend.auth.dto.out;

import org.example.sharesportsbackend.auth.vo.out.SignInResponseVo;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SignInResponseDto {

    private String accessToken;
    private String name;
    private String memberUuid;

    @Builder
    public SignInResponseDto(String accessToken, String name, String memberUuid) {
        this.accessToken = accessToken;
        this.name = name;
        this.memberUuid = memberUuid;
    }

    public SignInResponseVo toVo() {
        return SignInResponseVo.builder()
                .accessToken(accessToken)
                .name(name)
                .uuid(memberUuid)
                .build();
    }
}
