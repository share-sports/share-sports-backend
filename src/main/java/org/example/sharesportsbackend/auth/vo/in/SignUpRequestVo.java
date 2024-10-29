package org.example.sharesportsbackend.auth.vo.in;

import java.util.Date;

import org.example.sharesportsbackend.auth.domain.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Getter
@ToString
public class SignUpRequestVo {

    @Schema(description = "이메일", example = "test1234@gmail.com")
    private String email;

    @Schema(description = "비밀번호", example = "!test1234")
    private String password;

    @Schema(description = "이름", example = "홍길동")
    private String name;

    @Schema(description = "생년월일", example = "1990-01-01")
    private Date birth;

    @Schema(description = "역할", example = "MEMBER")
    private Role role;

    @Builder
    public SignUpRequestVo(String email, String password, String name, Date birth, Role role) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.birth = birth;
        this.role = role;
    }

}
