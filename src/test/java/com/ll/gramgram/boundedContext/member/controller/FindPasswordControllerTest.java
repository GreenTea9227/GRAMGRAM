package com.ll.gramgram.boundedContext.member.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest
class FindPasswordControllerTest {

    @Autowired
    MockMvc mvc;

    @Test
    @DisplayName("비밀번호 get요청 성공")
    void findPassword() throws Exception {
        mvc.perform(get("/usr/member/findPassword"))
                .andDo(print())
                .andExpect(handler().handlerType(MemberController.class))
                .andExpect(handler().methodName("findPassword"))
                .andExpect(status().isOk())
                .andExpect(view().name("usr/member/find-password"));
    }

    @Test
    @DisplayName("존재하지 않은 이메일로 비밀번호 변경 요청시 실패")
    void testFindPassword() throws Exception {
        mvc.perform(post("/usr/member/findPassword")
                        .with(csrf())
                        .param("email", "naver@naver.com"))
                .andExpect(handler().handlerType(MemberController.class))
                .andExpect(handler().methodName("findPassword"))
                .andExpect(status().is4xxClientError());

    }
}
