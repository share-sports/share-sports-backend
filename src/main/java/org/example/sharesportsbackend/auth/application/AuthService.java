package org.example.sharesportsbackend.auth.application;

import org.example.sharesportsbackend.auth.domain.AuthUserDetails;
import org.example.sharesportsbackend.auth.dto.in.AuthCodeRequestDto;
import org.example.sharesportsbackend.auth.dto.in.AuthRequestDto;
import org.example.sharesportsbackend.auth.dto.in.PasswordRequestDto;
import org.example.sharesportsbackend.auth.dto.in.SignInRequestDto;
import org.example.sharesportsbackend.auth.dto.in.SignUpRequestDto;
import org.example.sharesportsbackend.auth.dto.out.JwtTokenResponseDto;
import org.example.sharesportsbackend.auth.vo.in.RefreshTokenRequestDto;

import jakarta.servlet.http.HttpSession;

public interface AuthService {

    /**
     * AuthUserDetails service interface
     * 1. signUp
     * 2. signIn
     * 3. refreshAccessToken
     * 3. signOut
     */

    /**
     * 1. Sign up
     * Save user
     * @param signUpRequestDto
     * return void
     */
    void signUp(SignUpRequestDto signUpRequestDto);

    /**
     * 2. Sign in
     * Authenticate user
     * @param signInRequestDto
     * return signInResponseDto
     */
    JwtTokenResponseDto signIn(SignInRequestDto signInRequestDto);

    /**
     * 3. RefreshAccessToken
     * @param refreshTokenRequestDto
     * return JwtTokenResponseDto
     */
    JwtTokenResponseDto refreshAccessToken(RefreshTokenRequestDto refreshTokenRequestDto);

    /**
     * 4. Sign out
     * @param authUserDetails
     * return void
     */
    void signOut(AuthUserDetails authUserDetails);
    void sendPasswordResetEmail(AuthRequestDto authRequestDto);
    void sendAuthCodeEmail(HttpSession session);
    void verifyAuthCode(AuthCodeRequestDto dto, HttpSession session);
    void changePassword(PasswordRequestDto passwordRequestDto, String memberUUid);
}
