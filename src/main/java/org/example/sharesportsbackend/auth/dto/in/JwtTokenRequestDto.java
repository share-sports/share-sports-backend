package org.example.sharesportsbackend.auth.dto.in;

import org.example.sharesportsbackend.auth.vo.in.JwtTokenRequestVo;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class JwtTokenRequestDto {

    private String email;
    private String password;

    @Builder
    public JwtTokenRequestDto(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public static JwtTokenRequestDto from(JwtTokenRequestVo jwtTokenRequestVo) {
        return JwtTokenRequestDto.builder()
                .email(jwtTokenRequestVo.getEmail())
                .password(jwtTokenRequestVo.getPassword())
                .build();
    }
}
