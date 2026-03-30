package com.lmc.backend.filter;

import jakarta.persistence.criteria.*;
import org.jspecify.annotations.Nullable;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BaseSpecification<T> implements Specification<T> {
    protected final List<Predicate> predicates = new ArrayList<>();

    protected void addEqual(CriteriaBuilder cb, Root<T> root, String field, Object value) {
        if (value != null) {
            predicates.add(cb.equal(root.get(field), value));
        }
    }

    protected void addLike(CriteriaBuilder cb, Root<T> root, String field, String value) {
        if (value != null && !value.isBlank()) {
            predicates.add(
                    cb.like(
                            cb.lower(root.get(field)),
                            "%" + value.trim().toLowerCase() + "%"
                    )
            );
        }
    }

    protected void addBoolean(CriteriaBuilder cb, Root<T> root, String field, Boolean value) {
        if (value != null) {
            predicates.add(cb.equal(root.get(field), value));
        }
    }

    protected void addIn(CriteriaBuilder cb, Root<T> root, String field, Collection<?> values) {
        if (values != null && !values.isEmpty()) {
            predicates.add(root.get(field).in(values));
        }
    }

    protected <Y extends Comparable<? super Y>> void addGreaterThanOrEqualTo(
            CriteriaBuilder cb,
            Root<T> root,
            String field,
            Y value
    ) {
        if (value != null) {
            Path<Y> path = root.get(field);
            predicates.add(cb.greaterThanOrEqualTo(path, value));
        }
    }

    protected <Y extends Comparable<? super Y>> void addLessThanOrEqualTo(
            CriteriaBuilder cb,
            Root<T> root,
            String field,
            Y value
    ) {
        if (value != null) {
            Path<Y> path = root.get(field);
            predicates.add(cb.lessThanOrEqualTo(path, value));
        }
    }

    protected Predicate build(CriteriaBuilder cb) {
        return predicates.isEmpty()
                ? cb.conjunction()
                : cb.and(predicates.toArray(new Predicate[0]));
    }

    @Override
    public @Nullable Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        return null;
    }
}
