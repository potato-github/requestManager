package com.requestManager.util;

import com.requestManager.concurrent.ConcurrentExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * TODO
 *
 * @author chunxu.zhang
 * @since 2023/7/20
 **/
@Slf4j
@Component
public class IPPool implements ApplicationListener<ApplicationReadyEvent> {

    private static Queue<String> queue = new LinkedBlockingQueue<>();
    @Value("${ip.pool.percent}")
    private double percent;
    @Value("${ip.pool.url}")
    private String url;
    private AtomicInteger size = new AtomicInteger(0);
    private int totalSize;
    private static volatile AtomicBoolean initFlag = new AtomicBoolean(Boolean.FALSE);
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ConcurrentExecutor executor;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        initProxy();
    }

    public void offer(String e) {
        queue.offer(e);
        size.incrementAndGet();
    }

    public String poll() {
        String e = queue.poll();
        size.decrementAndGet();
        return e;
    }

    public String peek() {
        return queue.peek();
    }

    public void clear() {
        queue.clear();
        totalSize = 0;
    }

    public String getProxy(boolean isDelete) {
        String proxy = isDelete ? poll() : peek();
        if (size.get() <= percent * totalSize) {
            executor.getExecutor().execute(() -> initProxy());
        }
        return proxy;
    }

    public void initProxy() {
        if (initFlag.get()) {
            return;
        }
        boolean success = initFlag.compareAndSet(Boolean.FALSE, Boolean.TRUE);
        if (!success) {
            return;
        }
        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
            if (response.getStatusCode().value() != 200) {
                log.error("[IPPool] init fail");
                return;
            }
            String body = response.getBody();
            assert body != null;
            String[] ips = body.split("\r\n");
            clear();
            Arrays.stream(ips).forEach(this::offer);
            totalSize = ips.length;
            log.info("[IPPool] init success, size:{}", totalSize);
        } catch (Exception e) {
            log.error("[IPPool] init exception:{}", e.getMessage());
        } finally {
            initFlag.compareAndSet(Boolean.TRUE, Boolean.FALSE);
        }
    }

    // 每2分钟刷新一次proxy
    @Scheduled(cron = "0 */2 * * * ?")
    public void scheduled() {
        System.out.println(System.currentTimeMillis());
        initProxy();
    }


}
