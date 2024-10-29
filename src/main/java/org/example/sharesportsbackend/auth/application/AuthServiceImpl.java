package org.example.sharesportsbackend.auth.application;

import org.example.sharesportsbackend.auth.domain.AuthUserDetails;
import org.example.sharesportsbackend.auth.dto.in.AuthCodeRequestDto;
import org.example.sharesportsbackend.auth.dto.in.AuthRequestDto;
import org.example.sharesportsbackend.auth.dto.in.PasswordRequestDto;
import org.example.sharesportsbackend.auth.dto.in.SignInRequestDto;
import org.example.sharesportsbackend.auth.dto.in.SignUpRequestDto;
import org.example.sharesportsbackend.auth.dto.out.JwtTokenResponseDto;
import org.example.sharesportsbackend.auth.vo.in.RefreshTokenRequestDto;
import org.example.sharesportsbackend.global.common.UuidGenerator;
import org.example.sharesportsbackend.global.common.jwt.JwtTokenProvider;
import org.example.sharesportsbackend.global.common.response.BaseResponseStatus;
import org.example.sharesportsbackend.global.error.BaseException;
import org.example.sharesportsbackend.member.domain.Member;
import org.example.sharesportsbackend.member.infrastructure.MemberRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@RequiredArgsConstructor
@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

    private final MemberRepository memberRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    /**
     * AuthServiceImpl
     * 1. 회원가입
     * 2. 로그인
     * 3. 로그아웃
     */

    /**
     * 1. 회원가입
     * Save user
     *
     * @param signUpRequestDto return void
     */
    @Override
    @Transactional
    public void signUp(SignUpRequestDto signUpRequestDto) {

        log.info("signUpRequestDto : {}", signUpRequestDto);
        memberRepository.save(signUpRequestDto.toEntity(passwordEncoder));
    }

    /**
     * 2. 로그인
     * Authenticate user
     *
     * @param signInRequestDto return signInResponseDto
     */
    @Override
    @Transactional
    public JwtTokenResponseDto signIn(SignInRequestDto signInRequestDto) {

        log.info("jwtTokenRequestDto : {}", signInRequestDto);
        Member member = memberRepository.findByEmail(signInRequestDto.getEmail()).orElseThrow(
                () -> new BaseException(BaseResponseStatus.FAILED_TO_LOGIN)
        );

        try {
            JwtTokenResponseDto jwtTokenResponseDto = createToken(authenticate(member, signInRequestDto.getPassword()));
            jwtTokenResponseDto.setName(member.getName());
            log.info("token : {}", jwtTokenResponseDto);

            return jwtTokenResponseDto;
        } catch (Exception e) {
            throw new BaseException(BaseResponseStatus.FAILED_TO_LOGIN);
        }

    }

    // 임시 비밀번호 보내는 메서드
    public void sendPasswordResetEmail(AuthRequestDto authRequestDto) {
        Member member = memberRepository.findByEmail(authRequestDto.getEmail()).orElseThrow(() -> new BaseException(BaseResponseStatus.NOT_FOUND_DATA));

        // 임시 비밀번호로 password 변경
        String temporaryPassword = UuidGenerator.generateTemporaryPassword();
        memberRepository.save(authRequestDto.changeTemporaryPassword(passwordEncoder, member, temporaryPassword));

        // 변경 후 임시비밀번호를 알려주는 email 발송을 위한 email token 생성
        String token = jwtTokenProvider.buildEmailToken(authRequestDto.getEmail());

        // 임시 비밀번호 인증 이메일 보내기
        emailService.sendTemporaryPasswordEmail(authRequestDto.getEmail(), temporaryPassword, token);
    }

    // 인증코드 보내는 메서드
    public void sendAuthCodeEmail(HttpSession session) {

        String token = jwtTokenProvider.buildEmailToken((String) session.getAttribute("email"));
        emailService.sendAuthCodeEmail((String) session.getAttribute("email"), (String) session.getAttribute("authCode"), token);
    }

    // 인증코드 검증 메서드
    public void verifyAuthCode(AuthCodeRequestDto authCodeRequestDto, HttpSession session) {
        String authCode = (String) session.getAttribute("authCode");
        if (authCode == null || !authCode.equals(authCodeRequestDto.getAuthCode())) {
            throw new BaseException(BaseResponseStatus.AUTH_CODE_INVALID);
        }
    }

    // 비밀번호 변경 메서드
    public void changePassword(PasswordRequestDto passwordRequestDto, String memberUuid) {
        Member member = memberRepository.findByMemberUuid(memberUuid).orElseThrow(() -> new BaseException(BaseResponseStatus.NOT_FOUND_DATA));
        memberRepository.save(passwordRequestDto.changePassword(passwordEncoder, member, passwordRequestDto));
    }


    private JwtTokenResponseDto createToken(Authentication authentication) {
        return jwtTokenProvider.generateToken(authentication);
    }


    private Authentication authenticate(Member member, String inputPassword) {
        AuthUserDetails authUserDetail = new AuthUserDetails(member);
        return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authUserDetail.getUsername(),
                        inputPassword,
                        authUserDetail.getAuthorities()
                )
        );
    }

    /**
     * 3. RefreshAccessToken
     *
     * @param refreshTokenRequestDto return JwtTokenResponseDto
     */
    @Override
    public JwtTokenResponseDto refreshAccessToken(RefreshTokenRequestDto refreshTokenRequestDto) {
        return jwtTokenProvider.refreshAccessToken(refreshTokenRequestDto.getRefreshToken());
    }

    /**
     * 4. Sign out
     *
     * @param authUserDetails return void
     */
    @Override
    public void signOut(AuthUserDetails authUserDetails) {
        jwtTokenProvider.deleteRefreshToken(authUserDetails.getUsername());
    }

}
