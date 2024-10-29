package org.example.sharesportsbackend.auth.vo.in;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SignInRequestVo {

    @Schema(description = "이메일", example = "test1234@gmail.com")
    private String email;

    @Schema(description = "비밀번호", example = "!test1234")
    private String password;

    @Builder
    public SignInRequestVo(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
