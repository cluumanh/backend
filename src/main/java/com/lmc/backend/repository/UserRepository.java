package com.lmc.backend.repository;

import com.lmc.backend.enity.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends BaseRepository<User, Long> {
    boolean existsByUsername(String username);
    Optional<User> findByUsername(String username);
}
