package com.nal.service;

import com.nal.core.entity.WorkEntity;
import com.nal.exception.TodoException;
import com.nal.form.WorkForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface WorkService {

    Boolean isExistsWorkName(String workName);

    WorkEntity findById(Long id);

    Page<WorkEntity> findAll(Pageable pageable);

    WorkEntity add(WorkForm workForm) throws TodoException;

    WorkEntity edit(Long id, WorkForm workForm) throws TodoException;

    void delete(Long id) throws TodoException;
}
