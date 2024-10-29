package org.example.sharesportsbackend.auth.application;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class EmailService {


    private final JavaMailSender javaMailSender;

    public void sendTemporaryPasswordEmail(String email, String temporaryPassword, String token) {

        String subject = "임시 비밀번호 발급";
        String resetLink = "http://your-website.com/reset-password?token=" + token;
        String body = "귀하의 임시 비밀번호는 다음과 같습니다: " + temporaryPassword +
                "\n비밀번호를 재설정하려면 다음 링크를 클릭하세요: " + resetLink +
                "\n로그인 후 비밀번호를 변경해주세요.";

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject(subject);
        message.setText(body);

        javaMailSender.send(message);

    }

    public void sendAuthCodeEmail(String email, String authCode, String token) {
        String subject = "비밀번호 변경 인증코드";
        String resetLink = "http://your-website.com/reset-password?token=" + token; //
        String body = "귀하의 인증코드는" + authCode + "입니다.";

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject(subject);
        message.setText(body);

        javaMailSender.send(message);
    }

}
