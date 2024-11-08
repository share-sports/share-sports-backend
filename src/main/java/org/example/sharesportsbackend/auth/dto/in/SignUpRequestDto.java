package org.example.sharesportsbackend.auth.dto.in;

import java.util.Date;

import org.example.sharesportsbackend.auth.domain.Role;
import org.example.sharesportsbackend.auth.vo.in.SignUpRequestVo;
import org.example.sharesportsbackend.global.common.UuidGenerator;
import org.example.sharesportsbackend.member.domain.Member;
import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class SignUpRequestDto {

    private String email;
    private String password;
    private String name;
    private Date birth;
    private Role role;

    public Member toEntity(PasswordEncoder passwordEncoder) {
        return Member.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .memberUuid(UuidGenerator.generateMemberUuid())
                .name(name)
                .role(role)
                .build();
    }

    @Builder
    public SignUpRequestDto(
            String email,
            String password,
            String name,
            Date birth,
            Role role
    ) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.birth = birth;
        this.role = role;
    }

    public static SignUpRequestDto from(SignUpRequestVo signUpRequestVo) {
        return SignUpRequestDto.builder()
                .email(signUpRequestVo.getEmail())
                .password(signUpRequestVo.getPassword())
                .name(signUpRequestVo.getName())
                .birth(signUpRequestVo.getBirth())
                .role(signUpRequestVo.getRole())
                .build();
    }
}
