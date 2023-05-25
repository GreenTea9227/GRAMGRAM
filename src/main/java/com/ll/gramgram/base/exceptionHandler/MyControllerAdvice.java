package com.ll.gramgram.base.exceptionHandler;

import com.ll.gramgram.base.rq.Rq;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.task.TaskRejectedException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.nio.file.AccessDeniedException;

@Slf4j
@RequiredArgsConstructor
@ControllerAdvice
public class MyControllerAdvice {

    private final Rq rq;

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(TaskRejectedException.class)
    public String exceedCapacity(HttpServletRequest request, Exception e) {

        log.error("TaskRejectedException 발생", e);
        log.info("TaskRejectedException 발생 | [사용자 IP] ={}", request.getRemoteAddr());

        return rq.historyBack("잠시 후에 다시 이용해 주세요");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NotSupportUserLoginException.class)
    public String tryNotSupportedLogin(HttpServletRequest request, Exception e) {

        log.error("NotSupportUserLoginException 발생", e);
        log.info("NotSupportUserLoginException 발생 | [사용자 IP] ={}", request.getRemoteAddr());

        return rq.historyBack(e.getMessage());
    }

    @ExceptionHandler(DataNotFoundException.class)
    public String dataNotFoundException(Exception e) {

        log.info("dataNotFoundException 발생 ", e);

        return rq.historyBack(e.getMessage());
    }

    @ExceptionHandler(NotOverTimeException.class)
    public String notOverTimeException(Exception e) {

        log.info("NotOverTimeException 발생 ", e);

        return rq.historyBack(e.getMessage());
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public String handleNotFoundError(Model model, NoHandlerFoundException ex) {
        model.addAttribute("exception", ex);
        return "error/404";
    }

    @ExceptionHandler(AccessDeniedException.class)
    public String accessDeniedByException(Exception e) {

        log.info("accessDeniedByException 발생 ", e);

        return rq.historyBack("먼저 본인의 인스타 아이디를 입력해주세요.");
    }
}
