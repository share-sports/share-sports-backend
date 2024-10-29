package org.example.sharesportsbackend.auth.vo.in;

import org.example.sharesportsbackend.auth.dto.in.AuthRequestDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AuthRequestVo {

    @Schema(description = "이메일", example = "test1234@gmail.com", nullable = true)
    private String email;

    public static AuthRequestDto toDto(AuthRequestVo authRequestVo) {
        return AuthRequestDto.builder()
                .email(authRequestVo.getEmail())
                .build();
    }

}
