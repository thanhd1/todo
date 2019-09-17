package com.nal.api.service;

import com.nal.configuration.WebConfig;
import com.nal.core.entity.WorkEntity;
import com.nal.core.enums.WorkStatus;
import com.nal.core.repository.WorkRepository;
import com.nal.exception.TodoBadRequestException;
import com.nal.exception.TodoDataNotFoundException;
import com.nal.form.WorkForm;
import com.nal.service.impl.WorkServiceImpl;
import com.nal.utils.WebConvertUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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
    public void test_add_success() {
        WorkForm form = new WorkForm();
        form.setWorkName("Task 01");
        form.setStatus(WorkStatus.PLANNING);

        when(workRepository.save(any())).thenReturn(WebConvertUtil.formToEntity(form, new WorkEntity()));

        WorkEntity result = workService.add(form);
        assertEquals("Task 01", result.getWorkName());
        assertEquals(WorkStatus.PLANNING, result.getStatus());
    }

    @Test
    public void test_add_error() {
        when(workRepository.existsByWorkName(any())).thenReturn(false);

        WorkForm form = new WorkForm();
        form.setWorkName("Task 01");
        form.setStatus(WorkStatus.PLANNING);

        when(workRepository.save(any())).thenReturn(WebConvertUtil.formToEntity(form, new WorkEntity()));

        try {
            workService.add(form);
        } catch (Exception e) {
            assertTrue(e instanceof TodoBadRequestException);
            assertEquals("Task 01 already exists!", e.getMessage());
        }
    }

    @Test
    public void test_edit_success() {
        WorkForm form = new WorkForm();
        form.setWorkName("Task 01");
        form.setStatus(WorkStatus.DOING);

        WorkEntity workEntity = WebConvertUtil.formToEntity(form, new WorkEntity());

        when(workRepository.findByIdAndDeleted(1L, false)).thenReturn(Optional.of(workEntity));
        when(workRepository.save(any())).thenReturn(workEntity);

        WorkEntity result = workService.edit(1L, form);
        assertEquals("Task 01", result.getWorkName());
        assertEquals(WorkStatus.DOING, result.getStatus());
    }

    @Test
    public void test_edit_dataNotFound() {
        WorkForm form = new WorkForm();
        form.setWorkName("Task 01");
        form.setStatus(WorkStatus.DOING);

        when(workRepository.findByIdAndDeleted(1L, false)).thenReturn(Optional.ofNullable(null));

        try {
            workService.edit(1L, form);
        } catch (Exception e) {
            assertTrue(e instanceof TodoDataNotFoundException);
            assertEquals("Work[1] doesn't exist", e.getMessage());
        }
    }

    @Test
    public void test_edit_workNameUnique() {
        WorkForm form = new WorkForm();
        form.setWorkName("Task 02");
        form.setStatus(WorkStatus.DOING);

        WorkEntity workEntity = new WorkEntity();
        workEntity.setWorkName("Task 01");
        workEntity.setStatus(WorkStatus.DOING);

        when(workRepository.findByIdAndDeleted(1L, false)).thenReturn(Optional.of(workEntity));

        when(workRepository.existsByWorkName(form.getWorkName())).thenReturn(true);

        when(workRepository.save(any())).thenReturn(workEntity);

        try {
            workService.edit(1L, form);
        } catch (Exception e) {
            assertTrue(e instanceof TodoBadRequestException);
            assertEquals("Task 02 already exists!", e.getMessage());
        }
    }

    @Test
    public void test_delete_success() {
        WorkForm form = new WorkForm();
        form.setWorkName("Task 02");
        form.setStatus(WorkStatus.DOING);

        WorkEntity workEntity = new WorkEntity();
        workEntity.setWorkName("Task 01");
        workEntity.setStatus(WorkStatus.DOING);

        when(workRepository.findByIdAndDeleted(1L, false)).thenReturn(Optional.of(workEntity));

        workService.delete(1L);

        verify(workRepository, times(1)).realDelete(any());
    }

    @Test
    public void test_delete_dataNotFound() {

        when(workRepository.findByIdAndDeleted(1L, false)).thenReturn(Optional.ofNullable(null));

        try {
            workService.delete(1L);
        } catch (Exception e) {
            assertTrue(e instanceof TodoDataNotFoundException);
            assertEquals("Work[1] doesn't exist", e.getMessage());
        }
    }
}
