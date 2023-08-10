package com.requestManager.config;

import lombok.Data;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Map;

@Data
@Component
@ConfigurationProperties(prefix = "user")
public class UserLevelConfig implements ApplicationListener<ApplicationReadyEvent> {
    private Map<Integer, Integer> level;
    public int getLimitCount(int userId) {
        return level.get(userId);
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        level.put(0, Integer.MAX_VALUE);
    }
}
