package com.nal.service;

import com.nal.core.entity.WorkEntity;
import com.nal.exception.TodoException;
import com.nal.form.WorkForm;

import java.util.List;

public interface WorkService {

    Boolean isExistsWorkName(String workName);

    WorkEntity findById(Long id);

    List<WorkEntity> findAll();

    WorkEntity add(WorkForm workForm) throws TodoException;

    WorkEntity edit(Long id, WorkForm workForm) throws TodoException;

    void delete(Long id) throws TodoException;
}
