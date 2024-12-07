package com.tanx.blog.utils;

import cn.hutool.core.util.StrUtil;
import com.tanx.blog.exception.DataOperationErrorException;
import com.tanx.blog.function.DetermineFunction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @description:
 * @project_name: blog-server
 * @author: 谭翔
 * @date: 2023/11/18 11:47
 */

@Slf4j
@Component
public class FunctionUtils<T, K> {

    public static <T> DetermineFunction.ThrowFunction<T> isError(boolean flag) {
        return (message) -> {
            if (flag) {
                throw new DataOperationErrorException(message);
            }
        };
    }

    public static <T> DetermineFunction.ThrowFunction<T> isNull(T flag) {
        return (message) -> {
            if (flag == null) {
                throw new DataOperationErrorException(message);
            }
        };
    }

    public static <T> DetermineFunction.IfThrowAvoidFunction<T> isNullAVoid(T flag) {
        return (message, trueHandler) -> {
            if (flag == null) {
                throw new DataOperationErrorException(message);
            }
            trueHandler.accept(flag);
        };
    }

    public static <T> DetermineFunction.IfAVoidFunction isTrue(boolean data) {
        return (trueHandle) -> {
            if (data) {
                trueHandle.run();
            }
        };
    }


    public static <T> DetermineFunction.IfElseAVoidFunction<T, T> isTrueOrFalse(boolean flag) {
        return (trueHandle, falseHandle) -> {
            if (flag) {
                trueHandle.run();
            } else {
                falseHandle.run();
            }
        };
    }


    public static <T> DetermineFunction.IfThrowFunction<T> isTrue(T data) {
        return (message, trueHandle) -> {
            if (data instanceof String && StrUtil.isBlank((String) data)) {
                throw new DataOperationErrorException(message);
            } else if (data == null) {
                throw new DataOperationErrorException(message);
            }
            return trueHandle.apply(data);
        };
    }


    public static <T> DetermineFunction.IfThrowFunction<T> isTrue(boolean flag, T data) {
        return (message, trueHandle) -> {
            if (flag) {
                return trueHandle.apply(data);
            }
            throw new DataOperationErrorException(message);
        };
    }

    public static <T> DetermineFunction.IfElseFunction<T, T> isTrueOrFalse(boolean flag, T pageUtils) {
        return (trueHandle, falseHandle) -> {
            if (!flag) {
                return falseHandle.apply(pageUtils);
            }
            return trueHandle.apply(pageUtils);
        };
    }

    public static <T> DetermineFunction.IfFunction<T> isTrueOrNull(boolean flag, T pageUtils) {
        return (trueHandle) -> {
            if (!flag) {
                return null;
            }
            return trueHandle.apply(pageUtils);
        };
    }
}
