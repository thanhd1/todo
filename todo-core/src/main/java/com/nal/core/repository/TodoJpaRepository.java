package com.nal.core.repository;

import com.nal.core.entity.AbstractEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface TodoJpaRepository<E extends AbstractEntity, ID extends java.io.Serializable> extends
    JpaRepository<E, ID>, JpaSpecificationExecutor<E> {

    Optional<E> findByIdAndDeleted(ID id, boolean deleted);

    List<E> findAllByIdAndDeleted(Iterable<ID> ids, boolean deleted);

    List<E> findAllByDeleted(boolean deleted);

    Page<E> findAllByDeleted(boolean deleted, Pageable pageable);

    Page<E> findAllByIdAndDeleted(Iterable<ID> ids, boolean deleted, Pageable pageable);

    /**
     * Physically deletes the given entities.
     *
     * @param entities
     * @throws IllegalArgumentException in case the given {@link Iterable} is {@literal null}.
     */
    void realDeleteAll(Iterable<E> entities);

    /**
     * Physically deletes the entity with the given id.
     *
     * @param id must not be {@literal null}.
     * @throws IllegalArgumentException in case the given {@code id} is {@literal null}
     */
    void realDeleteById(ID id);

    /**
     * Physically deletes a given entity.
     *
     * @param entity
     * @throws IllegalArgumentException in case the given entity is {@literal null}.
     */
    void realDelete(E entity);

    /**
     * Physically deletes the given entities.
     *
     * @param entities
     * @throws IllegalArgumentException in case the given {@link Iterable} is {@literal null}.
     */
    void realDeleteInBatch(Iterable<E> entities);

    /**
     * Remove the given entity from the persistence context, causing
     * a managed entity to become detached. Unflushed changes made
     * to the entity if any (including removal of the entity),
     * will not be synchronized to the database.  Entities which
     * previously referenced the detached entity will continue to
     * reference it.
     *
     * @param entity entity instance
     * @throws IllegalArgumentException if the instance is not an entity
     */
    void detach(Object entity);

    /**
     * Clear the persistence context, causing all managed
     * entities to become detached. Changes made to entities that
     * have not been flushed to the database will not be
     * persisted.
     */
    void clear();
}
