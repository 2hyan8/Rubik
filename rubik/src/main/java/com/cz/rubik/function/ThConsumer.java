package com.cz.rubik.function;

public interface ThConsumer<T, U> {
    void accept(T t, U u1, U u2);
}
