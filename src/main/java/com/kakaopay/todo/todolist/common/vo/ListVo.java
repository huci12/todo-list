package com.kakaopay.todo.todolist.common.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * 공통 리스트 객체를 위한 Value Object
 * @param <T>
 */
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
