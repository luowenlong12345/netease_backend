package com.netease.homework.service.impl;

import com.netease.homework.domain.Role;
import com.netease.homework.domain.User;
import com.netease.homework.repository.RoleRepository;
import com.netease.homework.repository.UserRepository;
import java.util.List;
import com.netease.homework.service.UserService;
import com.netease.homework.utils.exception.GlobalException;
import com.netease.homework.utils.result.ResultCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserRepository userRepository;
    @Resource
    private RoleRepository roleRepository;
    @Resource
    @Lazy
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByUsername(username);
        if (user == null)
            throw new UsernameNotFoundException("user[" + username + "] NOT EXISTS!");
        return user;
    }

    @Override
    public User register(User user, Integer roleId) {
        if (userRepository.existsByUsername(user.getUsername()))
            throw new GlobalException(ResultCode.USER_ACCOUNT_ALREADY_EXIST);
        addRole(user, roleId);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public User addRole(User user, Integer roleId) {
        Role role = roleRepository.getRoleById(roleId);
        if (role == null) throw new GlobalException(ResultCode.ENTITY_NOT_EXIST);
        if(user.getRoles() == null)
            user.setRoles(new ArrayList<>());
        for (Role i: user.getRoles()){
            if (i.getId().equals(roleId))
                return user;
        }
        user.getRoles().add(role);
        userRepository.save(user);
        return user;
    }

    @Override
    public User addMoney(BigDecimal money) {

        return addMoney(getCurrentUser(), money);
    }

    @Override
    public User addMoney(User user, BigDecimal money) {
        user.setMoney(user.getMoney().add(money));
        userRepository.save(user);
        return user;
    }

    @Override
    public User addRole(Integer roleId){
        User user = getCurrentUser();
        addRole(user, roleId);
        return user;
    }
    @Override
    public User getCurrentUser(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findUserByUsername(username);
    }

    @Override
    public Map<String, Object> info() {
        User user = getCurrentUser();
        List<Map<String, Object>> roles = new ArrayList<>();
        for (Role role: user.getRoles()) {
            roles.add(new HashMap<>(){{
                put("id", role.getId());
                put("name", role.getName());
            }});
        }
        return new HashMap<>(){{
            put("username", user.getUsername());
            put("id", user.getId());
            put("money", user.getMoney());
            put("role", roles);
        }};
    }
}
