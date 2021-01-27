package com.netease.homework.controller;

import com.netease.homework.domain.User;
import com.netease.homework.service.UserService;
import com.netease.homework.utils.result.JsonResult;
import com.netease.homework.utils.result.ResultCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.security.Principal;

@Controller
@RequestMapping("api/user")
@Validated
@Transactional
public class UserController {
    private UserService userService;

    @Autowired @Lazy
    public UserController(UserService userService) {
        this.userService = userService;
    }


    @RequestMapping(value = "register", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult<Object> register(@Valid User user, @NotNull() Integer roleId){
        user.setMoney(new BigDecimal(0));
        userService.register(user, roleId);
        return new JsonResult<>(true);
    }

        @PostMapping(value = "addRole")
    @ResponseBody
    public JsonResult<Object> addRole(@NotNull() Integer roleId){
        userService.addRole(roleId);
        return new JsonResult<>(true);
    }

    @PostMapping(value = "recharge")
    @ResponseBody
    public JsonResult<Object> recharge(@NotNull() BigDecimal money){
        JsonResult<Object> result;
        if (money.compareTo(new BigDecimal(0)) < 0)
            result = new JsonResult<>(false, ResultCode.PARAM_NOT_VALID);
        else {
            userService.addMoney(money);
            result = new JsonResult<>(true);
        }
        return result;
    }

    @GetMapping(value = "info")
    @ResponseBody
    public JsonResult<Object> info(){
        JsonResult<Object> result;
        result = new JsonResult<>(true, userService.info());
        return result;
    }


}
