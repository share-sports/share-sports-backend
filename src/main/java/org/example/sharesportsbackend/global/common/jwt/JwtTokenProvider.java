package org.example.sharesportsbackend.global.common.jwt;

import java.security.Key;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import org.example.sharesportsbackend.auth.application.AuthUserDetailService;
import org.example.sharesportsbackend.auth.dto.out.JwtTokenResponseDto;
import org.example.sharesportsbackend.global.common.response.BaseResponseStatus;
import org.example.sharesportsbackend.global.error.BaseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtTokenProvider {

    @Value("${spring.jwt.access-token-validity}")
    private long accessTokenValidityInMilliseconds;

    @Value("${spring.jwt.refresh-token-validity}")
    private long refreshTokenValidityInMilliseconds;

    @Value("${spring.jwt.secret}")
    private String secret;

    private final RedisTemplate<String, String> redisTemplate;
    private final AuthUserDetailService authUserDetailService;


    /**
     * TokenProvider
     * 1. 토큰에서 uuid 가져오기
     * 2. Claims에서 원하는 claim 값 추출 ( - JWT version 업데이트로 사용하지 않음)
     * 3. 토큰에서 모든 claims 추출 ( - JWT version 업데이트로 사용하지 않음)
     * 4. 액세스 토큰 생성
     * 5. refresh 토큰 생성
     */

    /**
     * 1. 토큰에서 uuid 가져오기
     *
     * @param token
     * @return jwt토큰에서 추출한 사용자 UUID 반환
     * 토큰에서 추출한 클레임에서 사용자 UUID를 추출하여 반환합니다.
     */
    public String validateAndGetUserUuid(String token) throws BaseException {
        try {
            log.info(extractClaim(token, Claims::getSubject));
            return extractClaim(token, Claims::getSubject);
        } catch (NullPointerException e) {
            log.info("토큰에 담긴 유저 정보가 없습니다");
            throw new BaseException(BaseResponseStatus.WRONG_JWT_TOKEN);
        }
    }

    /**
     * 2. Claims에서 원하는 claim 값 추출
     *
     * @param token
     * @param claimsResolver jwt토큰에서 추출한 정보를 어떻게 처리할지 결정하는 함수
     * @return jwt토큰에서 모든 클레임(클레임은 토큰에 담긴 정보 의미 ) 추출한 다음 claimsResolver함수를 처리해 결과 반환
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaims(token);
        log.info("claims={}", claims);
        return claimsResolver.apply(claims);
    }

    /**
     * 3. 토큰에서 모든 claims 추출
     *
     * @param token 주어진 JWT 토큰에서 모든 클레임을 추출하여 반환합니다.
     *              토큰의 서명을 확인하기 위해 사용할 서명 키(getSigningKey())를 설정하고 토큰을 파싱하여 클레임들을 추출합니다.
     */
    private Claims extractAllClaims(String token) {
        log.info("extractAllClaims token={}", token);
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSignKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            // 이 단계에서 token의 유효성 검사 및 만료일 검사를 실시한다!
        }
        // 파싱 예외 처리
        catch (ExpiredJwtException e) {
            log.error("만료된 토큰입니다");
            throw new BaseException(BaseResponseStatus.WRONG_JWT_TOKEN);
        } catch (UnsupportedJwtException e) {
            log.error("지원되지 않는 유형의 토큰입니다");
            throw new BaseException(BaseResponseStatus.WRONG_JWT_TOKEN);
        } catch (MalformedJwtException | IllegalArgumentException e) {
            log.error("잘못된 토큰입니다");
            throw new BaseException(BaseResponseStatus.WRONG_JWT_TOKEN);
        } catch (io.jsonwebtoken.security.SignatureException e) {
            log.error("SecretKey가 일치하지 않습니다");
            throw new BaseException(BaseResponseStatus.WRONG_JWT_TOKEN);
        }
    }

    /**
     * 4. 액세스 토큰 생성
     *
     * @param authentication 사용자 정보
     * @return 클레임 정보와 사용자 정보를 기반으로 jwt 토큰 생성
     * 클레임 정보, 사용자 ID, 생성 시간, 만료 시간 등을 설정하고, 서명 알고리즘과 서명 키를 사용하여 토큰을 생성합니다.
     * Access Token 역활
     */
    public JwtTokenResponseDto generateToken(Authentication authentication) {
        // 권한 가져오기
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse("");
        long now = (new Date()).getTime();

        // Access Token 생성
        Date accessTokenExpireIn = new Date(now + accessTokenValidityInMilliseconds);
        String accessToken = buildToken(authentication.getName(), authorities, accessTokenExpireIn);

        // Refresh Token 생성
        Date refreshTokenExpireIn = new Date(now + refreshTokenValidityInMilliseconds);

        String refreshToken = buildToken(authentication.getName(), authorities, refreshTokenExpireIn);

        // Refresh Token을 Redis에 저장
        storeRefreshToken(authentication.getName(), refreshToken);

        return JwtTokenResponseDto.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .memberUuid(authentication.getName())
                .name(authentication.getName())
                .build();
    }

    public String buildEmailToken(String email){
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 15 * 60 * 1000))
                .signWith(SignatureAlgorithm.HS512,secret)
                .compact();
    }

    public Key getSignKey() {
        if (secret == null) {
            throw new BaseException(BaseResponseStatus.WRONG_JWT_TOKEN);
        }
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    // AccessToken 갱신
    public JwtTokenResponseDto refreshAccessToken(String refreshToken) {
        Claims claims = extractAllClaims(refreshToken);
        String memberUuid = claims.getSubject();

        // RT 토큰 유효성 확인
        validateRefreshToken(refreshToken, memberUuid);

        String authorities = getUserAuthorities(memberUuid);
        long now = new Date().getTime();
        Date accessTokenExpireIn = new Date(now + accessTokenValidityInMilliseconds);
        String newAccessToken = buildToken(memberUuid, authorities, accessTokenExpireIn);

        return JwtTokenResponseDto.builder()
                .grantType("Bearer")
                .accessToken(newAccessToken)
                .refreshToken(refreshToken)
                .build();
    }
//
    private void validateRefreshToken(String refreshToken, String username) {
        if (username == null || username.isEmpty()) {
            log.error("JWT 검증 실패: 사용자 이름이 null이거나 비어 있음.");
            throw new BaseException(BaseResponseStatus.WRONG_JWT_TOKEN);
        }

        String savedRefreshToken = redisTemplate.opsForValue().get(username);

        if (savedRefreshToken == null) {
            log.error("JWT 검증 실패: Redis에서 저장된 Refresh Token을 찾을 수 없음. 사용자: {}", username);
            throw new BaseException(BaseResponseStatus.WRONG_JWT_TOKEN);
        }

        if (!refreshToken.equals(savedRefreshToken)) {
            log.error("JWT 검증 실패: Refresh Token이 일치하지 않음. 사용자: {}", username);
            throw new BaseException(BaseResponseStatus.WRONG_JWT_TOKEN);
        }

        Claims claims = extractAllClaims(refreshToken);
        if (claims.getExpiration().before(new Date())) {
            log.error("JWT 검증 실패: Refresh Token이 만료됨. 사용자: {}", username);
            redisTemplate.delete(username);
            throw new BaseException(BaseResponseStatus.WRONG_JWT_TOKEN);
        }
    }


    /**
     *
     * @param subject 사용자 UUID
     * @param authorities 사용자 권한
     * @param expiration 토큰 만료 시간
     * @return
     *
     * claims에 subject(사용자 식별 정보) 포함시키기
     */
    private String buildToken(String subject, String authorities, Date expiration) {

        Claims claims = Jwts.claims().setSubject(subject);
        log.info("subject={}", subject);
        Date now = new Date();

        JwtBuilder builder = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(getSignKey());

        if (authorities != null) {
            builder.claim("auth", authorities);
        }

        log.debug("권한 확인: {}" , authorities);

        return builder.compact();
    }

    private void storeRefreshToken(String memberUuid, String refreshToken) {
        try {
            redisTemplate.opsForValue().set(memberUuid, refreshToken, refreshTokenValidityInMilliseconds, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            log.error("Failed to store Refresh Token in Redis", e);
            throw new BaseException(BaseResponseStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private String getUserAuthorities(String memberUuid) {

        UserDetails userDetails = authUserDetailService.loadUserByUsername(memberUuid);
        return userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElseThrow(() -> new BaseException(BaseResponseStatus.WRONG_JWT_TOKEN));
    }

    // Redis에 저장된 RefreshToken을 삭제
    public void deleteRefreshToken(String memberUuid) {
        redisTemplate.delete(memberUuid);
    }
}
