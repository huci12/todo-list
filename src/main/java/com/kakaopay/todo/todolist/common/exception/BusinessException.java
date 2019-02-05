package com.kakaopay.todo.todolist.common.exception;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class BusinessException extends RuntimeException{
    private String code;

    public BusinessException(String code){
        this.code = code;
    }
}
