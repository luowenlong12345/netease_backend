package com.netease.homework.repository;

import com.netease.homework.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findUserByUsername(String username);
    Boolean existsByUsername(String username);
}
