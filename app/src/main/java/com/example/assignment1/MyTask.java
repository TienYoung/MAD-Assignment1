package com.example.assignment1;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import android.os.Build;

public abstract class MyTask<Result> implements Callable<Result> {
    protected abstract void postExecute(Result result);

    protected void execute() {
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            CompletableFuture<Result> ret = CompletableFuture.supplyAsync(() -> {
                try {
                    return this.call();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }, executorService);
            CompletableFuture.allOf(ret).thenRun(() -> {
                postExecute(ret.join());
                executorService.shutdown();
            });
        }
    }
}