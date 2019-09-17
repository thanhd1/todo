package com.nal.service.impl;

import com.nal.core.entity.WorkEntity;
import com.nal.core.repository.WorkRepository;
import com.nal.exception.TodoBadRequestException;
import com.nal.exception.TodoDataNotFoundException;
import com.nal.exception.TodoException;
import com.nal.form.WorkForm;
import com.nal.service.WorkService;
import com.nal.utils.WebConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@Scope("prototype")
public class WorkServiceImpl implements WorkService {

    @Autowired
    private WorkRepository workRepository;

    @Override
    public WorkEntity findById(Long id) {
        Optional<WorkEntity> optional = workRepository.findByIdAndDeleted(id, false);
        if (!optional.isPresent()) {
            throw new TodoDataNotFoundException(String.format("Work[%s] doesn't exist", id));
        }

        return optional.get();
    }

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
    public WorkEntity add(WorkForm workForm) throws TodoException {
        if (isExistsWorkName(workForm.getWorkName())) {
            throw new TodoBadRequestException(workForm.getWorkName() + " already exists!");
        }

        return save(workForm, new WorkEntity());
    }

    @Override
    @Transactional
    public WorkEntity edit(Long id, WorkForm workForm) throws TodoException {

        WorkEntity entity = findById(id);
        if (!entity.getWorkName().equals(workForm.getWorkName()) && isExistsWorkName(workForm.getWorkName())) {
            throw new TodoBadRequestException(workForm.getWorkName() + " already exists!");
        }

        return save(workForm, entity);
    }

    @Override
    @Transactional
    public void delete(Long id) throws TodoException {
        WorkEntity entity = findById(id);
        workRepository.realDelete(entity);
    }

    private WorkEntity save(WorkForm workForm, WorkEntity workEntity) {
        WebConvertUtil.formToEntity(workForm, workEntity);
        return workRepository.save(workEntity);
    }
}
