package com.kakaopay.todo.todolist.app.vo;

import com.kakaopay.todo.todolist.common.validation.Create;
import com.kakaopay.todo.todolist.common.validation.Delete;
import com.kakaopay.todo.todolist.common.validation.Update;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

/**
 * 할일 상세 Value Object
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
public class TodoJobVo {
    // 작업 ID
    @NotEmpty(groups = {Update.class}, message = "TODO.VALIDATION.001")
    private String jobId;

    // 작업 내용
    @NotEmpty(groups = {Create.class, Update.class}, message = "TODO.VALIDATION.002")
    private String jobContent;

    // 작업 등록일
    private String regDate;

    // 작업 수정일
    private String updDate;

    // 내가 참조한 작업 ID 리스트 (수정에서 사용하기 위함)
    private List<String> listReferJobId = new ArrayList<>();

    // 나를 참조한 작업 ID 리스트 (리스트에 보여지기 위함)
    private List<String> listReferencedJobId = new ArrayList<>();

    // 완료 여부
    @NotEmpty(groups = {Delete.class}, message = "TODO.VALIDATION.003")
    private String completeYn;
}
