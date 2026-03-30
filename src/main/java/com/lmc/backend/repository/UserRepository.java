package com.lmc.backend.repository;

import com.lmc.backend.entity.User;
import com.lmc.backend.filter.UserSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends BaseRepository<User, Long> {
    boolean existsByUsername(String username);
    Optional<User> findByUsername(String username);
    @Query("select u from users")
    Page<User> findUsers(Pageable pageable);
}
