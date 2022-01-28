package com.cz.rubik.function;

public interface FouConsumer<T, U> {
    void accept(T t, U u1, U u2, U u3);
}
