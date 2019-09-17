package com.nal.core.repository.impl;

import com.nal.core.entity.AbstractEntity;
import com.nal.core.repository.TodoJpaRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@NoRepositoryBean
public class GenericJpaRepository<E extends AbstractEntity, ID extends Serializable> extends
    SimpleJpaRepository<E, ID> implements TodoJpaRepository<E, ID> {

    private static final int BATCH_SIZE = 50;
    private final EntityManager entityManager;
    private final JpaEntityInformation<E, ID> entityInformation;

    public GenericJpaRepository(JpaEntityInformation<E, ID> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.entityManager = entityManager;
        this.entityInformation = entityInformation;
    }

    @Override
    public Optional<E> findByIdAndDeleted(ID id, boolean deleted) {
        return super.findOne(
            Specification.where(new ByIdSpecification<>(entityInformation, id)).and(isDeleted(deleted)));
    }

    @Override
    public List<E> findAllByDeleted(boolean deleted) {
        return super.findAll(isDeleted(deleted));
    }

    @Override
    public Page<E> findAllByDeleted(boolean deleted, Pageable pageable) {
        return super.findAll(isDeleted(deleted), pageable);
    }

    @Override
    public List<E> findAllByIdAndDeleted(Iterable<ID> ids, boolean deleted) {
        Assert.notNull(ids, "The given Iterable of Id's must not be null!");
        ByIdsSpecification<E, ID> specification = new ByIdsSpecification<>(entityInformation, ids);
        return findAll(Specification.where(specification).and(isDeleted(deleted)));
    }

    @Override
    public Page<E> findAllByIdAndDeleted(Iterable<ID> ids, boolean deleted, Pageable pageable) {
        Assert.notNull(ids, "The given Iterable of Id's must not be null!");
        ByIdsSpecification<E, ID> specification = new ByIdsSpecification<>(entityInformation, ids);
        return super.findAll(Specification.where(specification).and(isDeleted(deleted)), pageable);
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public void deleteById(ID id) {
        this.delete(findById(id).orElseThrow(() -> new EmptyResultDataAccessException(
                String.format("No %s entity with id %s exists!", entityInformation.getJavaType(), id), 1)));
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public void delete(E entity) {
        Assert.notNull(entity, "The entity must not be null!");
        entity.setDeleted(true);
        super.save(entity);
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public void deleteAll(Iterable<? extends E> entities) {
        Assert.notNull(entities, "The given Iterable of entities not be null!");

        for (E entity : entities) {
            this.delete(entity);
        }
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public void deleteInBatch(Iterable<E> entities) {
        Assert.notNull(entities, "The given Iterable of entities not be null!");
        Iterator<E> iterator = entities.iterator();
        if (!iterator.hasNext()) {
            return;
        }

        int i = 0;

        while (iterator.hasNext()) {
            this.delete(iterator.next());
            ++i;
            if (i % BATCH_SIZE == 0) {
                flush();
                clear();
            }
        }
    }

    @Override
    public void deleteAll() {
        throw new UnsupportedOperationException("deleteAll() unsupported");
    }

    @Override
    public void deleteAllInBatch() {
        throw new UnsupportedOperationException("deleteAllInBatch() unsupported");
    }

    @Override
    public void realDeleteById(ID id) {
        super.deleteById(id);
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public void realDelete(E entity) {
        super.delete(entity);
    }

    @Override
    public void realDeleteInBatch(Iterable<E> entities) {
        super.deleteInBatch(entities);
    }

    @Override
    public void realDeleteAll(Iterable<E> entities) {
        Assert.notNull(entities, "The given Iterable of entities not be null!");

        for (E entity : entities) {
            this.realDelete(entity);
        }
    }

    @Override
    public void detach(Object entity) {
        entityManager.detach(entity);
    }

    @Override
    public void clear() {
        entityManager.clear();
    }

    private Specification<E> isDeleted(boolean deleted) {
        return new ByDeletedSpecification<>(deleted);
    }

    @SuppressWarnings("NullableProblems")
    private static final class ByDeletedSpecification<T extends AbstractEntity> implements Specification<T> {

        private static final long serialVersionUID = 1L;
        private static final String DELETED_FIELD = "deleted";

        private final boolean deleted;

        ByDeletedSpecification(boolean deleted) {
            this.deleted = deleted;
        }

        @Override
        public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
            if (deleted) {
                return criteriaBuilder.isTrue(root.get(DELETED_FIELD));
            }
            return criteriaBuilder.isFalse(root.get(DELETED_FIELD));
        }
    }

    @SuppressWarnings("NullableProblems")
    private static final class ByIdSpecification<T extends AbstractEntity, ID extends Serializable> implements
        Specification<T> {

        private static final long serialVersionUID = 1L;
        private final JpaEntityInformation<T, ID> entityInformation;
        private final ID id;

        ByIdSpecification(JpaEntityInformation<T, ID> entityInformation, ID id) {
            this.entityInformation = entityInformation;
            this.id = id;
        }

        @Override
        public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
            final List<Predicate> predicates = new ArrayList<>();
            if (entityInformation.hasCompositeId()) {
                for (String s : entityInformation.getIdAttributeNames()) {
                    predicates.add(cb.equal(root.<ID>get(s), entityInformation.getCompositeIdAttributeValue(id, s)));
                }

                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
            return cb.equal(root.get(entityInformation.getIdAttribute()), id);
        }
    }

    @SuppressWarnings("NullableProblems")
    private static final class ByIdsSpecification<T extends AbstractEntity, ID extends Serializable> implements
        Specification<T> {

        private static final long serialVersionUID = 1L;
        private final JpaEntityInformation<T, ID> entityInformation;

        private Iterable<ID> ids;

        ByIdsSpecification(JpaEntityInformation<T, ID> entityInformation, Iterable<ID> ids) {
            this.entityInformation = entityInformation;
            this.ids = ids;
        }

        @Override
        public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
            Object[] objects = StreamSupport.stream(ids.spliterator(), false).toArray();
            return root.get(entityInformation.getIdAttribute()).in(objects);
        }
    }

}
