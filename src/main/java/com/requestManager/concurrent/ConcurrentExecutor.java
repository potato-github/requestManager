package com.requestManager.concurrent;

import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 用于一些需要异步操作，但并发不高的场景
 *
 * @author chunxu.zhang
 * @since 2023/7/21
 **/
@Component
public class ConcurrentExecutor {
    private final static int PROCESSOR_COUNT = Runtime.getRuntime().availableProcessors();
    private static ExecutorService executor =
            new ThreadPoolExecutor(0, PROCESSOR_COUNT * 5,
                    1, TimeUnit.MINUTES, new LinkedBlockingQueue<>(1000));

    public ExecutorService getExecutor() {
        return executor;
    }
}
