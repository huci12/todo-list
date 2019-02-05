package com.kakaopay.todo.todolist.app.dao;

import com.kakaopay.todo.todolist.app.vo.TodoJobVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * DB 연동을 위한 Mapper
 */
@Mapper
public interface TodoMapper {
    // 전체 할일 갯수를 가져옴
    public long selectTodoJobTotalCnt();

    // 전체 할일 ID 리스트를 가져옴
    public List<String> selectTodoJobIdAllList();

    // 범위에 따른 할일 리스트를 가져옴
    public List<TodoJobVo> selectTodoJobList(@Param("offset") long offset, @Param("length") long length);

    // 할일의 참조 대상 리스트를 가져옴
    public List<TodoJobVo> selectRefTodoJobList(@Param("jobId") String jobId);

    // 할일 상세 정보를 가져옴
    public TodoJobVo selectDetailTodoJob(@Param("jobId") String jobId);

    // 할일을 등록함
    public int insertTodoJob(@Param("jobInfo") TodoJobVo jobInfo);

    // 할일을 수정함
    public int updateTodoJob(@Param("jobId") String jobId, @Param("jobInfo") TodoJobVo jobInfo);

    // 할일을 삭제함
    public int deleteTodoJob(@Param("jobId") String jobId);

    // 할일의 완료 여부를 변경함
    public int updateCompleteYn(@Param("jobInfo") TodoJobVo jobInfo);

    // 할일에 참조 대상 추가
    public int insertTodoReferJobList(@Param("jobId") String jobId, @Param("jobRefId") String jobRefId);

    // 할일에 선택된 참조 대상 제거
    public int deleteUncheckedReferTodoJob(@Param("jobId") String jobId, @Param("listReferJobId") List<String> listRefJobId);

    // 할일에 전체 참조 대상 제거
    public int deleteAllTodoRefJobList(@Param("jobId") String jobId);

    // 완료 또는 비완료 변경 가능 여부 체크
    public String selectIsCompleteYnPossible(@Param("jobInfo") TodoJobVo jobInfo);
}
