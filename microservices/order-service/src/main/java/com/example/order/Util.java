package com.example.order;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class Util {
    static <T> T get(CompletableFuture<T> save) {
        try {
            return save.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
