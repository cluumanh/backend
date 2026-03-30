package com.lmc.backend.filter;

import com.lmc.backend.dto.UserFilter;
import com.lmc.backend.entity.User;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class UserSpecification extends BaseSpecification<User> {
    private final UserFilter filter;

    public UserSpecification(UserFilter filter) {
        this.filter = filter;
    }

    @Override
    public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        if (filter == null) {
            return cb.conjunction();
        }

        addEqual(cb, root, "id", filter.getId());
        addLike(cb, root, "username", filter.getUsername());
        addLike(cb, root, "email", filter.getEmail());
        addEqual(cb, root, "status", filter.getStatus());

        return build(cb);
    }
}
