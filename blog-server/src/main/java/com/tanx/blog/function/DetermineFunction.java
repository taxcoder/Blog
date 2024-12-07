package com.tanx.blog.function;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @description:
 * @project_name: blog-server
 * @author: 谭翔
 * @date: 2023/11/18 11:36
 */

public interface DetermineFunction<T> {

    @FunctionalInterface
    public interface IfElseFunction<T, K> {
        T trueOrFalseHandle(Function<T, K> trueHandle, Function<T, K> falseHandle);
    }

    @FunctionalInterface
    public interface IfElseAVoidFunction<T, K> {
        void trueOrFalseHandle(Runnable trueHandle, Runnable falseHandle);
    }

    @FunctionalInterface
    public interface IfFunction<T> {
        T trueHandle(Function<T, T> trueHandle);
    }

    @FunctionalInterface
    public interface IfAVoidFunction {
        void trueHandle(Runnable trueHandle);
    }


    @FunctionalInterface
    public interface IfThrowFunction<T> {
        T trueOrFalseHandle(String message, Function<T, T> trueHandle);
    }

    @FunctionalInterface
    public interface IfThrowAvoidFunction<T> {
        void trueOrFalseHandle(String message, Consumer<T> trueHandle);
    }


    @FunctionalInterface
    interface ThrowFunction<T> {
        void throwFunction(String message);
    }

}

