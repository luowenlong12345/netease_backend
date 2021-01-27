package com.netease.homework.repository;

import com.netease.homework.domain.Role;
import com.netease.homework.domain.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role getRoleById(Integer roleId);
    List<Role> findAll();
}
