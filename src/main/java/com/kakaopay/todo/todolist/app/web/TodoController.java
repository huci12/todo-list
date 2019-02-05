package com.kakaopay.todo.todolist.app.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 일반 호출에 대한 처리를 위한 Controller
 */
@RequestMapping("/todo-list")
@Controller
@Slf4j
public class TodoController {
    /**
     * 리스트 페이지 이동
     * @return
     */
    @GetMapping("")
    public String index(){
        return "list";
    }
}
