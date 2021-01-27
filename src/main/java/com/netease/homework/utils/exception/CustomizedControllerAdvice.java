package com.netease.homework.utils.exception;

import com.netease.homework.utils.result.JsonResult;
import com.netease.homework.utils.result.ResultCode;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.data.mapping.PropertyPath;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class CustomizedControllerAdvice {
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public JsonResult<Object> javaExceptionHandler(Exception ex) {//APIResponse是项目中对外统一的出口封装，可以根据自身项目的需求做相应更改
        JsonResult<Object> result = new JsonResult<>(false);
        result.setErrorMsg(ex.getMessage());
        return result;
    }

    @ExceptionHandler(BindException.class)
    @ResponseBody
    public JsonResult<Map<String, String>> bindExceptionHandler(BindException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        List<ObjectError> errors = bindingResult.getAllErrors();
       Map<String, String> map = new HashMap<>();
        for (ObjectError error : errors) {
            FieldError fieldError = (FieldError) error;
            String name = fieldError.getField();
            map.put(name, error.getDefaultMessage());
        }

        return new JsonResult<>(false, ResultCode.PARAM_NOT_VALID, map);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    public JsonResult<Map<String, String>> constraintViolationExceptionHandler(ConstraintViolationException ex) {
        Map<String, String> msg =new HashMap<>();
        ex.getConstraintViolations().forEach(error -> {
            String message = error.getMessage();
            PathImpl path = (PathImpl) error.getPropertyPath();
            String name = path.getLeafNode().getName();
            msg.put(name, message);
        });
        return new JsonResult<>(false, ResultCode.PARAM_NOT_VALID, msg);
    }

    @ExceptionHandler(GlobalException.class)
    @ResponseBody
    public JsonResult<Object> globalExceptionHandler(GlobalException ex) {
        JsonResult<Object> result = new JsonResult<>(false);
        result.setErrorMsg(ex.getMessage());
        result.setErrorCode(ex.getCode());
        return result;
    }



}
