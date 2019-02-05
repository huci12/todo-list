package com.kakaopay.todo.todolist.common.vo;

import lombok.*;

/**
 * 공통 에러 Value Object
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
public class ErrorVo {
    // Error 코드값
    private String code;

    // Error 메시지
    private String message;
}
