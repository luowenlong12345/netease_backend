package com.netease.homework.utils.userAuthentication;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONPObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.netease.homework.utils.result.JsonResult;
import com.netease.homework.utils.result.ResultCode;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@Component
public class LoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        JsonResult<Object> result;
        if (exception.getClass() == DisabledException.class)
            result = new JsonResult<>(false, ResultCode.USER_ACCOUNT_DISABLE);
        else if (exception.getClass() == LockedException.class)
            result = new JsonResult<>(false, ResultCode.USER_ACCOUNT_LOCKED);
        else if (exception.getClass() == AccountExpiredException.class)
            result = new JsonResult<>(false, ResultCode.USER_ACCOUNT_EXPIRED);
        else if (exception.getClass() == CredentialsExpiredException.class)
            result = new JsonResult<>(false, ResultCode.USER_CREDENTIALS_EXPIRED);
        else if (exception.getClass() == BadCredentialsException.class)
            result = new JsonResult<>(false, ResultCode.USER_CREDENTIALS_ERROR);
        else {
            result = new JsonResult<>(false);
            result.setErrorMsg(exception.getMessage());
        }
        response.setContentType("application/json;charset=utf8");
        PrintWriter out = response.getWriter();
        out.write(JSON.toJSONString(result, SerializerFeature.WriteMapNullValue));
    }
}
