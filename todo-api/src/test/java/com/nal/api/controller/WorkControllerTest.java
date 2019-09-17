package com.nal.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nal.configuration.WebConfig;
import com.nal.controller.WorkController;
import com.nal.core.entity.WorkEntity;
import com.nal.core.enums.WorkStatus;
import com.nal.form.WorkForm;
import com.nal.service.WorkService;
import com.nal.utils.WebConvertUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebAppConfiguration
@RunWith(PowerMockRunner.class)
@ContextConfiguration(classes = {WebConfig.class})
public class WorkControllerTest {

    private MockMvc mockMvc;

    @Mock
    private WorkService workService;

    @InjectMocks
    private WorkController workController;

    @Before
    public void init(){
        mockMvc = MockMvcBuilders.standaloneSetup(workController).build();
    }

    @Test
    public void test_createNewWork_success() throws Exception {

        String workName = "Task 01";
        WorkStatus workStatus = WorkStatus.PLANNING;

        WorkForm workForm = new WorkForm();
        workForm.setWorkName(workName);
        workForm.setStatus(workStatus);

        when(workService.add(any())).thenReturn(WebConvertUtil.formToEntity(workForm, new WorkEntity()));

        ObjectMapper mapper = new ObjectMapper();
        String requestJson = mapper.writeValueAsString(workForm);

        mockMvc.perform(
            post("/v1/works")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestJson))
            .andExpect(status().isOk())
            .andExpect(jsonPath("workName", is("Task 01")))
            .andExpect(jsonPath("status", is("PLANNING")));

        verify(workService, times(1)).add(any());

        verifyNoMoreInteractions(workService);
    }

    @Test
    public void test_createNewWork_error() throws Exception {

        String workName = "";
        WorkStatus workStatus = WorkStatus.PLANNING;

        WorkForm workForm = new WorkForm();
        workForm.setWorkName(workName);
        workForm.setStatus(workStatus);

        when(workService.add(any())).thenReturn(WebConvertUtil.formToEntity(workForm, new WorkEntity()));

        ObjectMapper mapper = new ObjectMapper();
        String requestJson = mapper.writeValueAsString(workForm);

        mockMvc.perform(
            post("/v1/works")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestJson))
            .andExpect(status().isBadRequest());

        verify(workService, times(0)).add(any());
    }

    @Test
    public void test_editWork_success() throws Exception {

        String workName = "Task 01";
        WorkStatus workStatus = WorkStatus.DOING;

        WorkForm workForm = new WorkForm();
        workForm.setWorkName(workName);
        workForm.setStatus(workStatus);

        when(workService.edit(any(), any())).thenReturn(WebConvertUtil.formToEntity(workForm, new WorkEntity()));

        ObjectMapper mapper = new ObjectMapper();
        String requestJson = mapper.writeValueAsString(workForm);

        mockMvc.perform(
            put("/v1/works/1")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestJson))
            .andExpect(status().isOk())
            .andExpect(jsonPath("workName", is("Task 01")))
            .andExpect(jsonPath("status", is("DOING")));

        verify(workService, times(1)).edit(any(), any());

        verifyNoMoreInteractions(workService);
    }

    @Test
    public void test_editWork_error() throws Exception {

        String workName = "";
        WorkStatus workStatus = WorkStatus.DOING;

        WorkForm workForm = new WorkForm();
        workForm.setWorkName(workName);
        workForm.setStatus(workStatus);

        when(workService.edit(any(), any())).thenReturn(WebConvertUtil.formToEntity(workForm, new WorkEntity()));

        ObjectMapper mapper = new ObjectMapper();
        String requestJson = mapper.writeValueAsString(workForm);

        mockMvc.perform(
            put("/v1/works/1")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestJson))
            .andExpect(status().isBadRequest());

        verify(workService, times(0)).edit(any(), any());
    }

    @Test
    public void test_deleteWork_success() throws Exception {
        mockMvc.perform(
            delete("/v1/works/1").contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isNoContent());

        verify(workService, times(1)).delete(any());
    }

    @Test
    public void test_findById_success() throws Exception {
        WorkEntity workEntity = new WorkEntity();
        workEntity.setWorkName("Task 01");
        workEntity.setStatus(WorkStatus.COMPLETE);

        when(workService.findById(any())).thenReturn(workEntity);

        mockMvc.perform(
            get("/v1/works/1").contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andExpect(jsonPath("workName", is("Task 01")))
            .andExpect(jsonPath("status", is("COMPLETE")));

        verify(workService, times(1)).findById(any());
    }

    @Test
    public void test_paging_success() throws Exception {

        LocalDateTime dateTime = LocalDateTime.of(2019, 9, 17, 0, 0, 0);

        List<WorkEntity> workEntities = Arrays.asList(
            new WorkEntity(1L, "Task 1", dateTime, dateTime, WorkStatus.PLANNING),
            new WorkEntity(2L, "Task 2", dateTime, dateTime, WorkStatus.DOING),
            new WorkEntity(3L, "Task 3", dateTime, dateTime, WorkStatus.COMPLETE));

        when(workService.findAll((any()))).thenReturn(new PageImpl<>(workEntities));

        mockMvc.perform(get("/v1/works")
                            .param("sortBy", "id")
                            .param("order", "asc")
                            .param("page", "0")
                            .param("pageSize", "10")
                            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content", hasSize(3)))
            .andExpect(jsonPath("$.content[0].id", is(1)))
            .andExpect(jsonPath("$.content[0].workName", is("Task 1")))
            .andExpect(jsonPath("$.content[0].status", is("PLANNING")))
            .andExpect(jsonPath("$.content[1].id", is(2)))
            .andExpect(jsonPath("$.content[1].workName", is("Task 2")))
            .andExpect(jsonPath("$.content[1].status", is("DOING")))
            .andExpect(jsonPath("$.content[2].id", is(3)))
            .andExpect(jsonPath("$.content[2].workName", is("Task 3")))
            .andExpect(jsonPath("$.content[2].status", is("COMPLETE")));

        verify(workService, times(1)).findAll(any());
    }

}
