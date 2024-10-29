package org.example.sharesportsbackend.auth.application;

import org.example.sharesportsbackend.auth.domain.AuthUserDetails;
import org.example.sharesportsbackend.global.common.response.BaseResponseStatus;
import org.example.sharesportsbackend.global.error.BaseException;
import org.example.sharesportsbackend.member.domain.Member;
import org.example.sharesportsbackend.member.infrastructure.MemberRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public AuthUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return memberRepository.findByEmail(username)
                .map(this::createUserDetails)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.NOT_FOUND_DATA));
    }

    private AuthUserDetails createUserDetails(Member member) {
        return AuthUserDetails.builder()
                .member(member)
                .build();
    }
}
