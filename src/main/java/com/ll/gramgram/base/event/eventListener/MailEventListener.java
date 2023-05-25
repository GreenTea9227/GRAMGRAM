package com.ll.gramgram.base.event.eventListener;

import com.ll.gramgram.boundedContext.member.dto.ChangePasswordDto;
import com.ll.gramgram.standard.mail.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class MailEventListener {

    private final MailService mailService;

    @Async("asyncThreadPool")
    @EventListener
    public void changePassword(ChangePasswordDto dto) {
        String email = dto.getEmail();
        String password = dto.getPassword();
        mailService.sendMail(email, "비밀번호 변경", password);
        log.info("이메일 보냄");
    }
}
