package com.ll.gramgram.standard.mail;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ActiveProfiles;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class MailServiceTest {

    @Mock
    private JavaMailSender javaMailSender;

    @InjectMocks
    private SmtpMailService mailService;

    @Test
    @DisplayName("1개의 메일 보내기 테스트")
    void sendMail() {
        // Given
        String email = "test@example.com";
        String subject = "Test Subject";
        String content = "Test Content";

        // When
        mailService.sendMail(email, subject, content);

        //then
        verify(javaMailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    @Timeout(1)
    @DisplayName("여러개의 메일 보내기 테스트")
    void sendMail2() {
        // Given

        String subject = "Test Subject";
        String content = "Test Content";

        // When
        for (int i = 0; i < 10; i++) {
            String email = "test" + i + "@example.com";
            mailService.sendMail(email, subject + i, content + i);

        }

        //then
        verify(javaMailSender, times(10)).send(any(SimpleMailMessage.class));
    }

}