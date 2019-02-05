package com.kakaopay.todo.todolist.common.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ListVo<T> {
    // 데이타
    private List<T> datas;

    // 총 개수
    private long totalCnt;
}
