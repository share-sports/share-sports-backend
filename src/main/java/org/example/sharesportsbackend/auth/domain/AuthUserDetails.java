package org.example.sharesportsbackend.auth.domain;

import java.util.Collection;
import java.util.List;

import org.example.sharesportsbackend.member.domain.Member;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@NoArgsConstructor
public class AuthUserDetails implements UserDetails {
    private String memberUuid;
    private String password;
    private String email;
    private Role role;

    @Builder
    public AuthUserDetails(Member member) {
        this.memberUuid = member.getMemberUuid();
        this.password = member.getPassword();
        this.email = member.getEmail();
        this.role = member.getRole();
    }

    /**
     * 단일 role을 사용할 경우 SimpleGrantedAuthority로 변환
     * 복수 role을 사용할 경우 GrantedAuthority로 변환
     * @return
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.memberUuid;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
