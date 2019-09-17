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
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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

}
