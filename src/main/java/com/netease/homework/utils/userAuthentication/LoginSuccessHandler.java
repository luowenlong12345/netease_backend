package com.netease.homework.utils.userAuthentication;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.netease.homework.domain.Role;
import com.netease.homework.domain.User;
import com.netease.homework.utils.result.JsonResult;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

@Component
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        User user = (User) authentication.getPrincipal();
        PrintWriter out = response.getWriter();
        List<Integer> roleIds = new ArrayList<>();
        for (Role role : user.getRoles())
            roleIds.add(role.getId());
        Map<String, Object> map = new HashMap<>();
        map.put("roles", roleIds);
        map.put("username", user.getUsername());
        map.put("money", user.getMoney());
        JsonResult<Object> result = new JsonResult<>(true, map);
        response.setContentType("application/json;charset=utf8");

        out.write(JSON.toJSONString(result, SerializerFeature.WriteMapNullValue));
    }
}
