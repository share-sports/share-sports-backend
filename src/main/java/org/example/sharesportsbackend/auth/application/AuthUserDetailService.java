package org.example.sharesportsbackend.auth.application;

import org.example.sharesportsbackend.auth.domain.AuthUserDetails;
import org.example.sharesportsbackend.member.infrastructure.MemberRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthUserDetailService implements UserDetailsService {

    private final MemberRepository memberRepository;
    @Override
    public UserDetails loadUserByUsername(String memberUuid) throws UsernameNotFoundException {
        return new AuthUserDetails(memberRepository.findByMemberUuid(memberUuid).orElseThrow(() -> new UsernameNotFoundException(memberUuid)));
    }

}

