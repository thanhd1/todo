package com.nal.service.impl;

import com.nal.core.entity.WorkEntity;
import com.nal.core.repository.WorkRepository;
import com.nal.exception.TodoBadRequestException;
import com.nal.exception.TodoException;
import com.nal.form.WorkForm;
import com.nal.service.WorkService;
import com.nal.utils.WebConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@Scope("prototype")
public class WorkServiceImpl implements WorkService {

    @Autowired
    private WorkRepository workRepository;

    @Override
    public List<WorkEntity> findAll() {
        return workRepository.findAllByDeleted(false);
    }

    @Override
    public Boolean isExistsWorkName(String workName) {
        return workRepository.existsByWorkName(workName);
    }

    @Override
    @Transactional
    public WorkEntity save(WorkForm workForm) throws TodoException {
        if (isExistsWorkName(workForm.getWorkName())) {
            throw new TodoBadRequestException(workForm.getWorkName() + " already exists!");
        }

        WorkEntity workEntity = new WorkEntity();
        WebConvertUtil.formToEntity(workForm, workEntity);

        return workRepository.save(workEntity);
    }
}
