package com.requestManager.util;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 滑动窗口限流
 *
 * @author chunxu.zhang
 * @since 2023/7/21
 **/
public class SlideWindow {
    private String key;
    private static Map<String, AtomicLong> countMap = new HashMap<>();

    private static Map<String, Queue<ReqOpr>> map = new HashMap<>();
    private int limitCount;
    private int limitTimeSecond;

    public SlideWindow(Long userId, String url, int limitCount, int limitTimeSecond) {
        this.key = userId + url;
        this.limitCount = limitCount;
        this.limitTimeSecond = limitTimeSecond;
        countMap.put(key, new AtomicLong(0L));
        map.put(key, new LinkedTransferQueue<>());
    }

    public boolean canOpr() {
        AtomicLong count = countMap.get(key);
        // 未满
        if (!isFull(count)) {
            return true;
        }

        Queue<ReqOpr> reqQueue = map.get(key);
        // 最后一个的时间>1h
        while ((System.currentTimeMillis() - reqQueue.peek().getTime()) > limitTimeSecond) {
            ReqOpr poll = reqQueue.poll();
            if (Objects.isNull(poll)) {
                count = new AtomicLong(0L);
                return true;
            }
            count.addAndGet(-poll.getCount());
        }
        // 只保留1h内，再查看是否full
        return isFull(count);
    }

    public boolean isFull(AtomicLong count) {
        return count.get() >= limitCount;
    }

    @Data
    static class ReqOpr {
        private Long time;
        private Long count;
    }
}


