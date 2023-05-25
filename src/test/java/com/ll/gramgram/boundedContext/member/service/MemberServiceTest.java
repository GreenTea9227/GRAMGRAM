package com.ll.gramgram.boundedContext.member.service;

import com.ll.gramgram.base.exceptionHandler.DataNotFoundException;
import com.ll.gramgram.base.rsData.RsData;
import com.ll.gramgram.boundedContext.member.dto.FindPasswordForm;
import com.ll.gramgram.boundedContext.member.entity.Member;
import com.ll.gramgram.boundedContext.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
public class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    private String email;
    private String password;
    private String username;

    @BeforeEach
    void setup() {
        email = "email@naver.com";
        password = "1234";
        username = "saveMember";
        Member saveMember = Member.builder()
                .password(password)
                .email(email)
                .username(username)
                .build();
        memberRepository.save(saveMember);
    }

    @Test
    @DisplayName("비밀번호 변경 요청 성공")
    void checkAndChangePassword() {

        //when
        FindPasswordForm findPasswordForm = new FindPasswordForm(email);
        RsData<String> result = memberService.checkAndChangePassword(findPasswordForm);

        //then
        Optional<Member> memberOptional = memberRepository.findByEmail(email);

        assertThat(result.getMsg()).isEqualTo("이메일을 확인하세요");
        assertThat(memberOptional.isPresent()).isTrue();
        assertThat(memberOptional.get().getPassword()).isNotEqualTo(password);
    }

    @Test
    @DisplayName("비밀번호 변경 요청 실패")
    void checkAndChangePasswordFail() {
        //given
        String notExistsEmail = "naver@naver.com";

        //when
        FindPasswordForm findPasswordForm = new FindPasswordForm(notExistsEmail);

        //then
        assertThatThrownBy(() -> memberService.checkAndChangePassword(findPasswordForm))
                .isInstanceOf(DataNotFoundException.class)
                .hasMessageContaining("존재하지 않는 이메일입니다.");

    }

}
