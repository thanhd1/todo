package com.nal.service.impl;

import com.nal.core.entity.WorkEntity;
import com.nal.core.repository.WorkRepository;
import com.nal.service.WorkService;
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
}
