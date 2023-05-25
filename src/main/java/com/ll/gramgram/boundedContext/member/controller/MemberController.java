package com.ll.gramgram.boundedContext.member.controller;

import com.ll.gramgram.base.rq.Rq;
import com.ll.gramgram.base.rsData.RsData;
import com.ll.gramgram.boundedContext.member.dto.FindPasswordForm;
import com.ll.gramgram.boundedContext.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/usr/member") // 액션 URL의 공통 접두어
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final Rq rq;

    @GetMapping("/login") // 로그인 폼, 로그인 폼 처리는 스프링 시큐리티가 구현, 폼 처리시에 CustomUserDetailsService 가 사용됨
    public String showLogin() {
        return "usr/member/login";
    }

    @GetMapping("/me") // 로그인 한 나의 정보 보여주는 페이지
    public String showMe(Model model) {
        return "usr/member/me";
    }

    @PreAuthorize("isAnonymous()")
    @GetMapping("/findPassword")
    public String findPassword(Model model) {
        model.addAttribute("passwordForm", new FindPasswordForm());
        return "usr/member/find-password";
    }

    @PreAuthorize("isAnonymous()")
    @PostMapping("/findPassword")
    public String findPassword(@Valid FindPasswordForm findPasswordForm, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return rq.historyBack("잘못된 이메일 형식입니다.");
        }

        RsData<String> result = memberService.checkAndChangePassword(findPasswordForm);

        if (result.isFail()) {
            return rq.historyBack(result);
        }

        return rq.redirectWithMsg("/member/login", "이메일을 확인하세요");
    }
}
