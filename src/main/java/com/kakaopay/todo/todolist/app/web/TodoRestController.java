package com.kakaopay.todo.todolist.app.web;

import com.kakaopay.todo.todolist.app.service.TodoService;
import com.kakaopay.todo.todolist.app.vo.TodoJobVo;
import com.kakaopay.todo.todolist.common.exception.BusinessException;
import com.kakaopay.todo.todolist.common.validation.Create;
import com.kakaopay.todo.todolist.common.validation.Update;
import com.kakaopay.todo.todolist.common.vo.ListVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Rest 호출에 대한 처리를 위한 Controller
 */
@RequestMapping("/api/v1/todo-list")
@RestController
@Slf4j
public class TodoRestController {
    @Autowired
    private TodoService todoService;

    /**
     * 전체 할일 ID 목록을 반환한다.
     * @return
     */
    @GetMapping("/ids")
    public ResponseEntity<ListVo> allIdList(){
        return ResponseEntity.ok(todoService.getTodoJobIdAllList());
    }

    /**
     * 페이지 번호에 따른 할일 목록을 반환한다.
     * @param pageNum
     * @return
     */
    @GetMapping({"", "/{pageNum}"})
    public ResponseEntity<ListVo> list(@PathVariable(name = "pageNum", required = false) int pageNum){
        log.debug("current page number: {}", pageNum);
        return ResponseEntity.ok(todoService.getTodoJobList(pageNum == 0 ? 1 : pageNum));
    }

    /**
     * 할일 상세를 반환한다.
     * @param jobId
     * @return
     */
    @GetMapping("/job/{jobId}")
    public ResponseEntity<TodoJobVo> detail(@PathVariable String jobId){
        TodoJobVo todoJobInfo = todoService.getDetailTodoJob(jobId);
        if(todoJobInfo != null){
            return ResponseEntity.ok(todoService.getDetailTodoJob(jobId));
        }
        log.debug("Job matched with jobId({}) is not exists!", jobId);
        throw new BusinessException("TODO.COMMON.002");
    }

    /**
     * 할일을 등록한다.
     * @param todoJobVo
     * @return
     */
    @PostMapping("/job")
    public ResponseEntity<String> insert(@RequestBody @Validated(Create.class) TodoJobVo todoJobVo){
        int ret = todoService.insertTodoJob(todoJobVo);
        log.debug("todo job: {}", todoJobVo);
        log.info("Insert cnt: {}", ret);
        if(ret > 0){
            return ResponseEntity.ok(null);
        }
        throw new BusinessException("TODO.COMMON.001");
    }

    /**
     * 할일을 수정한다.
     * @param todoJobVo
     * @return
     */
    @PutMapping("/job/{jobId}")
    public ResponseEntity<String> update(@PathVariable String jobId, @RequestBody @Validated(Update.class) TodoJobVo todoJobVo){
        int ret = todoService.updateTodoJob(jobId, todoJobVo);
        log.debug("todo job: {}", todoJobVo);
        log.info("Update cnt: {}", ret);
        if(ret > 0){
            return ResponseEntity.ok(null);
        }
        throw new BusinessException("TODO.COMMON.001");
    }

    /**
     * 할일의 완료 여부를 수정한다.
     * @param jobId
     * @param todoJobVo
     * @return
     */
    @PutMapping("/complete/{jobId}")
    public ResponseEntity<String> complete(@PathVariable String jobId, @RequestBody TodoJobVo todoJobVo){
        log.debug("todo job: {}", todoJobVo);
        String isPossible = todoService.getIsCompleteYnPossible(todoJobVo);
        // 완료 가능 혹은 불가능이 Exception을 발생할 상황은 아니므로 STATUS 200으로 던지고 추가로 플래그 값을 던진다.
        if(StringUtils.equals(isPossible, "Y")) {
            int ret = todoService.updateCompleteYn(jobId, todoJobVo);
            if(ret > 0){
                return ResponseEntity.ok(isPossible);
            }
        }else{
            return ResponseEntity.ok(isPossible);
        }
        throw new BusinessException("TODO.COMMON.001");
    }
}
