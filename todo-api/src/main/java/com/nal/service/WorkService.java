package com.nal.service;

import com.nal.core.entity.WorkEntity;
import com.nal.exception.TodoException;
import com.nal.form.WorkForm;

import java.util.List;

public interface WorkService {

    Boolean isExistsWorkName(String workName);

    List<WorkEntity> findAll();

    WorkEntity save(WorkForm workForm) throws TodoException;

}
