package org.example.sharesportsbackend.auth.presentation;

import org.example.sharesportsbackend.auth.application.AuthService;
import org.example.sharesportsbackend.auth.domain.AuthUserDetails;
import org.example.sharesportsbackend.auth.dto.in.PasswordRequestDto;
import org.example.sharesportsbackend.auth.dto.in.SignInRequestDto;
import org.example.sharesportsbackend.auth.dto.in.SignUpRequestDto;
import org.example.sharesportsbackend.auth.vo.in.AuthCodeRequestVo;
import org.example.sharesportsbackend.auth.vo.in.AuthRequestVo;
import org.example.sharesportsbackend.auth.vo.in.RefreshTokenRequestDto;
import org.example.sharesportsbackend.auth.vo.in.RefreshTokenRequestVo;
import org.example.sharesportsbackend.auth.vo.in.SignInRequestVo;
import org.example.sharesportsbackend.auth.vo.in.SignUpRequestVo;
import org.example.sharesportsbackend.auth.vo.out.JwtTokenResponseVo;
import org.example.sharesportsbackend.global.common.UuidGenerator;
import org.example.sharesportsbackend.global.common.response.BaseResponse;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "계정 관리 API", description = "계정 관련 API endpoints")
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    /**
     * api/auth
     * 1. 회원가입
     * 2. 로그인
     * 3. 로그아웃
     */

    @Operation(summary = "회원가입", description = """
            code: String, 6자리 이상
                
            password: 비밀번호는 최소 8자 이상, 영문자, 숫자, 특수문자를 각각 1개 이상 포함해야 합니다.
                
            role: STUDENT, PROFESSOR, ADMIN 중에서 택 1
            """)
    @PostMapping("/sign-up")
    public BaseResponse<Void> signUp(@RequestBody SignUpRequestVo signUpRequestVo) {
        log.info("signUpRequestVo : {}", signUpRequestVo);
        authService.signUp(SignUpRequestDto.from(signUpRequestVo));
        return new BaseResponse<>();
    }

    @Operation(summary = "로그인", description = """
            로그인 기능입니다.
            """)
    @PostMapping("/sign-in")
    public BaseResponse<JwtTokenResponseVo> signIn(@RequestBody SignInRequestVo signInRequestVo) {

        return new BaseResponse<>(
                authService.signIn(SignInRequestDto.from(signInRequestVo)).toVo()
        );
    }


    @Operation(summary = "Access Token 재발급", description = "Access Token 만료시 기존에 발급받은 Refresh Token을 이쪽으로 보내서 새로운 Access Token 받아가기")
    @PostMapping("/refresh")
    public BaseResponse<JwtTokenResponseVo> refreshAccessToken(@RequestBody RefreshTokenRequestVo refreshTokenRequestVo) {

        return new BaseResponse<>(
                authService.refreshAccessToken(RefreshTokenRequestDto.from(refreshTokenRequestVo)).toVo()
        );
    }

    @Operation(summary = "로그아웃", description = "현재 로그인 된 계정의 로그아웃")
    @PostMapping("/logout")
    public BaseResponse<Void> logout(@AuthenticationPrincipal AuthUserDetails authUserDetail) {
        authService.signOut(authUserDetail);
        return new BaseResponse<>();
    }

    @Operation(summary = "비밀번호 찾기", description = "이메일 인증을 통해 임시 비밀번호를 발급 받습니다.")
    @PostMapping("/password-reset")
    public BaseResponse<Void> resetPassword(@RequestBody AuthRequestVo emailRequestVo) {
        authService.sendPasswordResetEmail(AuthRequestVo.toDto(emailRequestVo)); // 이메일 토큰 생성
        return new BaseResponse<>();
    }

    @Operation(summary = "비밀번호 변경 인증 요청", description ="요청을 통해 이메일로 인증코드를 보냅니다.")
    @PostMapping("/password-change")
    public BaseResponse<Void> changePassword(HttpSession session, @AuthenticationPrincipal AuthUserDetails authUserDetails) {
        session.setAttribute("authCode", UuidGenerator.generateAuthCode());
        session.setAttribute("email",authUserDetails.getEmail());// 세션에 인증코드 저장
        //인증코드 이메일로 보내기
        authService.sendAuthCodeEmail(session);
        return new BaseResponse<>();
    }

    @Operation(summary = "인증코드 확인", description = "인증코드를 확인합니다.")
    @PostMapping("/verify-auth-code")
    public BaseResponse<Void> verifyAuthCode(@RequestBody AuthCodeRequestVo authCodeRequestVo, HttpSession session){
        authService.verifyAuthCode(AuthCodeRequestVo.toDto(authCodeRequestVo),session);
        return new BaseResponse<>();
    }

    @Operation(summary = "비밀번호 변경", description = "인증코드로 인증 후 비밀번호를 변경합니다.")
    @PostMapping("/change-password")
    public BaseResponse<Void> changePassword(@RequestBody PasswordRequestDto passwordRequestDto, HttpSession session, @AuthenticationPrincipal AuthUserDetails authUserDetails){
        session.removeAttribute("email");
        session.removeAttribute("authCode"); // 인증 코드 제거
        authService.changePassword(passwordRequestDto, authUserDetails.getMemberUuid());
        return new BaseResponse<>();
    }



}




