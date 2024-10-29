package org.example.sharesportsbackend.global.common.jwt;

import java.io.IOException;

import org.example.sharesportsbackend.auth.application.AuthUserDetailService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthUserDetailService userDetailService;

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String memberUuid;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // JWT 토큰에서 'Bearer ' 부분을 제거한 실제 토큰 추출
        jwt = authHeader.substring(7);

        try {
            // JWT를 파싱하고 클레임에서 memberUuid 추출
            memberUuid = jwtTokenProvider.validateAndGetUserUuid(jwt);

            log.info("memberUuid: {}", memberUuid);

            // 현재 SecurityContext에 인증 정보가 없을 경우 인증 처리
            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.userDetailService.loadUserByUsername(memberUuid);
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
            log.info("Authorities: {}", SecurityContextHolder.getContext().getAuthentication().getAuthorities());

        } catch (ExpiredJwtException e) {
            log.error("JWT가 만료되었습니다.", e);
        } catch (Exception e) {
            log.error("JWT 토큰을 처리하는 중 오류가 발생했습니다.", e);
        }

        filterChain.doFilter(request, response);
    }
}
