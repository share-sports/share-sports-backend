package org.example.sharesportsbackend.auth.dto.in;

import org.example.sharesportsbackend.auth.vo.in.SignInRequestVo;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SignInRequestDto {

    private String email;
    private String password;

    @Builder
    public SignInRequestDto(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public static SignInRequestDto from(SignInRequestVo signInRequestVo) {
        return SignInRequestDto.builder()
                .email(signInRequestVo.getEmail())
                .password(signInRequestVo.getPassword())
                .build();
    }
}
