package com.kakaopay.todo.todolist.app.web;

import com.google.gson.Gson;
import com.kakaopay.todo.todolist.app.vo.TodoJobVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TodoRestControllerTest {
    private MockMvc mockMvc;
    private Gson gson;

    @MockBean
    TodoRestController todoRestController;

    @Before
    public void setup(){
        gson = new Gson();
        mockMvc = MockMvcBuilders.standaloneSetup(todoRestController).build();
    }

    /**
     * 등록 테스트
     * @throws Exception
     */
    @Test
    public void test001InsertTodoJob() throws Exception{
        mockMvc.perform(post("/api/v1/todo-list/job")
                .contentType(MediaType.APPLICATION_JSON)
                .content(newTodoJob()))
                .andDo(print())
                .andExpect(status().isOk());
    }

    /**
     * 수정 테스트
     * @throws Exception
     */
    @Test
    public void test002UpdateTodoJob() throws Exception{
//        mockMvc.perform(put("/api/v1/todo-list/job/0008")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(updateTodoJob("0008")))
//                .andDo(print())
//                .andExpect(status().isOk());
    }

    /**
     * 삭제 테스트
     * @throws Exception
     */
    @Test
    public void test003DeleteTodoJob() throws Exception{
//        mockMvc.perform(delete("/api/v1/todo-list/job/0001")
//                .contentType(MediaType.APPLICATION_JSON))
//                .andDo(print())
//                .andExpect(status().isOk());
    }

    /**
     * 할일 전체 ID 리스트 테스트
     * @throws Exception
     */
    @Test
    public void test004GetAllIdList() throws Exception{
        //given(this.todoRestController.allIdList())
        //        .willReturn(ResponseEntity.ok(new ListVo()));

        mockMvc.perform(get("/api/v1/todo-list/ids"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    /**
     * 페이지 별 할일 리스트 테스트
     * @throws Exception
     */
    @Test
    public void test005GetTodoJobList() throws Exception{
        //given(this.todoRestController.list(Optional.of(1)))
        //        .willReturn(ResponseEntity.ok(new ListVo()));

        mockMvc.perform(get("/api/v1/todo-list/1"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    /**
     * 등록 테스트용
     * @return
     */
    private String newTodoJob(){
        TodoJobVo todoJob = new TodoJobVo();
        todoJob.setJobContent("JUNIT 할일 등록 테스트");
        return gson.toJson(todoJob);
    }

    /**
     * 수정 테스트용
     * @param jobId
     * @return
     */
    private String updateTodoJob(String jobId){
        TodoJobVo todoJob = new TodoJobVo();
        todoJob.setJobId(jobId);
        todoJob.setJobContent("JUNIT 할일 수정 테스트");

        List<String> listReferJobId = new ArrayList<>();
        listReferJobId.add("0001");
        listReferJobId.add("0002");
        listReferJobId.add("0003");
        todoJob.setListReferJobId(listReferJobId);

        return gson.toJson(todoJob);
    }
}
