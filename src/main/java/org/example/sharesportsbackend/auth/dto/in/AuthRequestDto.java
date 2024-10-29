package org.example.sharesportsbackend.auth.dto.in;

import org.example.sharesportsbackend.member.domain.Member;
import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class AuthRequestDto {

    private String email;

// 임시 비밀번호로 비밀번호 변경

    public Member changeTemporaryPassword(PasswordEncoder passwordEncoder, Member member, String temporaryPassword) {
        return Member.builder()
                .id(member.getId())
                .email(member.getEmail())
                .password(passwordEncoder.encode(temporaryPassword))
                .memberUuid(member.getMemberUuid())
                .name(member.getName())
                .role(member.getRole())
                .birth(member.getBirth()).build();
    }

    @Builder
    public AuthRequestDto(String email) {
        this.email = email;
    }
}
