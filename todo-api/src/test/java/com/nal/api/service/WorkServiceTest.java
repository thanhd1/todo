package com.nal.api.service;

import com.nal.configuration.WebConfig;
import com.nal.core.entity.WorkEntity;
import com.nal.core.enums.WorkStatus;
import com.nal.core.repository.WorkRepository;
import com.nal.exception.TodoBadRequestException;
import com.nal.form.WorkForm;
import com.nal.service.impl.WorkServiceImpl;
import com.nal.utils.WebConvertUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@WebAppConfiguration
@RunWith(PowerMockRunner.class)
@ContextConfiguration(classes = {WebConfig.class})
public class WorkServiceTest {

    @Mock
    private WorkRepository workRepository;

    @InjectMocks
    private WorkServiceImpl workService;

    @Test
    public void test_isExistsWorkName_isTrue() {
        when(workRepository.existsByWorkName(any())).thenReturn(true);

        assertTrue(workService.isExistsWorkName("Task 01"));
    }

    @Test
    public void test_isExistsWorkName_isFalse() {
        when(workRepository.existsByWorkName(any())).thenReturn(false);

        assertFalse(workService.isExistsWorkName("Task 02"));
    }

    @Test
    public void test_save_success() {
        WorkForm form = new WorkForm();
        form.setWorkName("Task 01");
        form.setStatus(WorkStatus.PLANNING);

        when(workRepository.save(any())).thenReturn(WebConvertUtil.formToEntity(form, new WorkEntity()));

        WorkEntity result = workService.save(form);
        assertEquals("Task 01", result.getWorkName());
        assertEquals(WorkStatus.PLANNING, result.getStatus());
    }

    @Test
    public void test_save_error() {
        when(workRepository.existsByWorkName(any())).thenReturn(false);

        WorkForm form = new WorkForm();
        form.setWorkName("Task 01");
        form.setStatus(WorkStatus.PLANNING);

        when(workRepository.save(any())).thenReturn(WebConvertUtil.formToEntity(form, new WorkEntity()));

        try {
            workService.save(form);
        } catch (Exception e) {
            assertTrue(e instanceof TodoBadRequestException);
            assertEquals("Task 01 already exists!", e.getMessage());
        }
    }
}
