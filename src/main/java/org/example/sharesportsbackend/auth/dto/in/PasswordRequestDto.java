package org.example.sharesportsbackend.auth.dto.in;

import org.example.sharesportsbackend.member.domain.Member;
import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class PasswordRequestDto {

    private String password;

    public Member changePassword(PasswordEncoder passwordEncoder, Member member, PasswordRequestDto passwordRequestDto) {
        return Member.builder()
                .id(member.getId())
                .email(member.getEmail())
                .password(passwordEncoder.encode(passwordRequestDto.getPassword()))
                .memberUuid(member.getMemberUuid())
                .name(member.getName())
                .role(member.getRole())
                .birth(member.getBirth()).build();
    }

    @Builder
    public PasswordRequestDto(String password) {
        this.password = password;
    }

}
