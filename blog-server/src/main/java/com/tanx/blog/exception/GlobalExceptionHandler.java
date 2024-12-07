package com.tanx.blog.exception;

import com.tanx.blog.utils.Response;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @description: 全局异常类
 * @project_name: blog-server
 * @author: 谭翔
 * @date: 2023/9/24 22:32
 */


@Slf4j
@ResponseBody
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({Exception.class, RuntimeException.class})
    public Response<?> globalException(Exception e) {
        e.printStackTrace();
        return Response.error(500, e.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Response<?> httpMessageNotReadableException(HttpMessageNotReadableException e) {
        return Response.error(500, "参数格式异常！");
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public Response<?> httpMessageNotReadableException(MethodArgumentTypeMismatchException e) {
        return Response.error(500, "参数格式异常！");
    }

    @ExceptionHandler(DataOperationErrorException.class)
    public Response<?> dataOperationErrorException(DataOperationErrorException e) {
        return Response.error(500, e.getMessage());
    }

    /* 用户名或密码错误 */
    @ExceptionHandler({BadCredentialsException.class, UsernameNotFoundException.class})
    public Response<?> badCredentialsException(Throwable e) {
        e.printStackTrace();
        return Response.error(500, "用户名或密码错误！");
    }


    @ExceptionHandler(ConstraintViolationException.class)
    public Response<?> constraintViolationException(ConstraintViolationException e) {
        return Response.error(412, Arrays.stream(e.getMessage().trim().split(",")).map(msg -> msg.split(":")[1].trim()).collect(Collectors.joining(";")));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Response<?> methodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<ObjectError> allErrors = e.getBindingResult().getAllErrors();
        String message = allErrors.stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining(";"));
        return Response.error(412, message);
    }

    @ExceptionHandler({NoTokenException.class, TokenVerifyErrorException.class, TokenFormatErrorException.class, TokenFailureException.class})
    public Response<?> token(Exception e) {
        return Response.error(403, e.getMessage());
    }
}
