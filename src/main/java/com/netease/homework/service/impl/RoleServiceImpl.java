package com.netease.homework.service.impl;

import com.netease.homework.domain.Role;
import com.netease.homework.repository.RoleRepository;
import com.netease.homework.service.RoleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RoleServiceImpl implements RoleService {
    @Resource
    RoleRepository roleRepository;

    @Override
    public List<Map<String, Object>> getAllRoles() {
        List <Map<String, Object>> res = new ArrayList<>();
        for (Role role: roleRepository.findAll()){
            res.add(new HashMap<>(){{
                put("id", role.getId());
                put("name", role.getName());
            }});
        }
        return res;
    }
}
