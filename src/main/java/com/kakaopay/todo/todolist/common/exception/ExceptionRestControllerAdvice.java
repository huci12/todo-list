package com.kakaopay.todo.todolist.common.exception;

import com.kakaopay.todo.todolist.common.vo.ErrorVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

/**
 * Rest 요청에 대한 에러 핸들러
 */
@Slf4j
@RestControllerAdvice(annotations = RestController.class)
public class ExceptionRestControllerAdvice {
    @Autowired
    private MessageSource messageSource;

    private MessageSourceAccessor accessor;

    @PostConstruct
    private void init() {
        accessor = new MessageSourceAccessor(messageSource, Locale.KOREA);
    }

    /**
     * 비즈니스 로직 관련 Exception Handler
     * @param req
     * @param ex
     * @return
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorVo> businessHandler(HttpServletRequest req, BusinessException ex){
        log.error("Request URL: {}", req.getRequestURI());
        return ResponseEntity.badRequest().body(getErrorVo(ex.getCode()));
    }

    /**
     * Validation 관련 Exception Handler
     * @param req
     * @param ex
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorVo> validationHandler(HttpServletRequest req, MethodArgumentNotValidException ex){
        log.error("Request URL: {}", req.getRequestURI());
        return ResponseEntity.badRequest().body(getErrorVo(ex.getBindingResult().getFieldError().getDefaultMessage()));
    }

    /**
     * 의도치 않은 Exception 관련 Exception Handler
     * @param req
     * @param ex
     * @return
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorVo> defaultHandler(HttpServletRequest req, Exception ex){
        log.error("Request URL: {}", req.getRequestURI());
        log.error("ERROR MESSAGE: {}", ex.getMessage());
        return ResponseEntity.badRequest().body(getErrorVo("TODO.COMMON.001"));
    }

    /**
     * 코드값에 따른 Error 객체를 생성
     * @param code
     * @return
     */
    private ErrorVo getErrorVo(String code){
        String message = accessor.getMessage(code);
        ErrorVo errorVo = new ErrorVo();
        errorVo.setCode(code);
        errorVo.setMessage(message);
        return errorVo;
    }
}
