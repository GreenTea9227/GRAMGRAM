package com.ll.gramgram.standard.mail;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class SmtpMailService implements MailService {

    private final JavaMailSender javaMailSender;

    public void sendMail(String email, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject(subject);
        message.setText(content);
        message.setTo(email);
        javaMailSender.send(message);
        log.info("mail을 {}에게 보내기 성공", email);
    }

}
