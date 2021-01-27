package com.netease.homework.controller;

import com.netease.homework.service.RoleService;
import com.netease.homework.utils.result.JsonResult;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.Map;
import java.util.List;

@Controller
@Transactional
@RequestMapping("/api/role")
public class RoleController {
    @Resource
    RoleService roleService;

    @GetMapping("/all")
    @ResponseBody
    public JsonResult<List<Map<String , Object>>> all() {
        return new JsonResult<>(true, roleService.getAllRoles());
    }

}
