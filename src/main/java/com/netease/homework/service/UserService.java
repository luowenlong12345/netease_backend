package com.netease.homework.service;

import com.netease.homework.domain.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.math.BigDecimal;
import java.util.Map;

public interface UserService extends UserDetailsService {
    User register(User user, Integer roleId);
    User addRole(Integer roleId);
    User addRole(User user, Integer roleId);
    User addMoney(BigDecimal money);
    User addMoney(User user, BigDecimal money);
    public User getCurrentUser();
    Map<String, Object> info();

}
