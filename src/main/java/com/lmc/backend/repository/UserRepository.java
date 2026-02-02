package com.lmc.backend.repository;

import com.lmc.backend.enity.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends BaseRepository<User, Long> {
    boolean existsByUsername(String username);
    User findByUsername(String username);
}
