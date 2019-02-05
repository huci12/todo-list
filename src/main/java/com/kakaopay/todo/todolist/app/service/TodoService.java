package com.kakaopay.todo.todolist.app.service;

import com.kakaopay.todo.todolist.app.dao.TodoMapper;
import com.kakaopay.todo.todolist.app.vo.TodoJobVo;
import com.kakaopay.todo.todolist.common.vo.ListVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Slf4j
@Service
public class TodoService {
    private final static int PAGE_PER_SIZE = 10;

    @Autowired
    private TodoMapper todoMapper;

    /**
     * 페이지 번호에 따른 할일 리스트를 가져옴
     * @param pageNum
     * @return
     */
    public ListVo getTodoJobList(int pageNum){
        // 페이지 번호에 따른 범위를 가져옴
        int offset = 0;
        if(pageNum == 1){
            offset = (pageNum - 1);
        }
        else{
            offset = (pageNum - 1) * PAGE_PER_SIZE;
        }

        long totalCnt = todoMapper.selectTodoJobTotalCnt();
        ListVo<TodoJobVo> listVo = new ListVo();
        listVo.setTotalCnt(totalCnt);

        // 결과가 있을 경우에만 리스트를 조회한다.
        if(totalCnt > 0){
            listVo.setDatas(todoMapper.selectTodoJobList(offset, PAGE_PER_SIZE));
        }

        log.debug("selected todo list size : {}", totalCnt);
        log.debug("result: {}", listVo);
        return listVo;
    }

    /**
     * 할일 전체 ID 리스트를 가져옴
     * @return
     */
    public ListVo getTodoJobIdAllList(){
        // 전체 할일 ID 리스트
        List<String> todoJobIdAllList = todoMapper.selectTodoJobIdAllList();
        long totalCnt = todoJobIdAllList.size();

        ListVo<String> listVo = new ListVo();
        listVo.setDatas(todoJobIdAllList);
        listVo.setTotalCnt(totalCnt);
        return listVo;
    }

    /**
     * 할일 ID에 따른 할일 상세를 가져옴
     * @param jobId
     * @return
     */
    public TodoJobVo getDetailTodoJob(String jobId){
        return todoMapper.selectDetailTodoJob(jobId);
    }

    /**
     * 할일을 등록함
     * @param jobInfo
     * @return
     */
    @Transactional
    public int insertTodoJob(TodoJobVo jobInfo){
        int ret = todoMapper.insertTodoJob(jobInfo);
        if(ret > 0){
            // 체크되지 않은 참조 할일을 지운다.
            todoMapper.deleteUncheckedReferTodoJob(jobInfo.getJobId(), jobInfo.getListReferJobId());

            log.debug("listReferJobId size: {}", jobInfo.getListReferJobId().size());

            // 참조 할일을 추가한다. (*기존에 없을 경우에만)
            jobInfo.getListReferJobId().forEach(jobRefId -> {
                todoMapper.insertTodoReferJobList(jobInfo.getJobId(), jobRefId);
            });
        }
        return ret;
    }

    /**
     * 할일을 수정함
     * @param jobId
     * @param jobInfo
     * @return
     */
    @Transactional
    public int updateTodoJob(String jobId, @RequestBody @Validated TodoJobVo jobInfo){
        int ret = todoMapper.updateTodoJob(jobId, jobInfo);
        if(ret > 0){
            // 체크되지 않은 참조 할일을 지운다.
            todoMapper.deleteUncheckedReferTodoJob(jobInfo.getJobId(), jobInfo.getListReferJobId());

            log.debug("listReferJobId size: {}", jobInfo.getListReferJobId().size());

            jobInfo.getListReferJobId().forEach(jobRefId -> {
                todoMapper.insertTodoReferJobList(jobInfo.getJobId(), jobRefId);
            });
        }
        return ret;
    }

    /**
     * 할일을 삭제함, 참조된 대상도 삭제함
     * @param jobId
     * @return
     */
    @Transactional
    public int deleteTodoJob(String jobId){
        int ret = todoMapper.deleteTodoJob(jobId);
        // 할일 삭제와 동시에 할일에 대한 참조 대상도 삭제한다.
        if(ret > 0){
            todoMapper.deleteAllTodoRefJobList(jobId);
        }
        return ret;
    }

    /**
     * 할일 완료 혹은 비완료 가능 여부를 반환함
     * @param jobInfo
     * @return
     */
    public String getIsCompleteYnPossible(TodoJobVo jobInfo){
        return todoMapper.selectIsCompleteYnPossible(jobInfo);
    }

    /**
     * 할일을 완료 혹은 비완료 상태로 변경함
     * @param jobId
     * @param jobInfo
     * @return
     */
    @Transactional
    public int updateCompleteYn(String jobId, TodoJobVo jobInfo){
        return todoMapper.updateCompleteYn(jobInfo);
    }
}
