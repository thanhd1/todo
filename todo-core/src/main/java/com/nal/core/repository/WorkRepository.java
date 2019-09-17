package com.nal.core.repository;

import com.nal.core.entity.WorkEntity;

import java.util.List;

public interface WorkRepository extends TodoJpaRepository<WorkEntity, Long>  {

    List<WorkEntity> findAllByDeleted(Boolean deleted);

}
