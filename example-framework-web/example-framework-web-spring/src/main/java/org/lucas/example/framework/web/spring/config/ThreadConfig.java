package org.lucas.example.framework.web.spring.config;

import org.lucas.component.thread.task.ThreadPoolTaskExecutor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@EnableAsync
public class ThreadConfig {

    private Integer queueCapacity = 128;

    @Bean
    @Primary
    public ThreadPoolTaskExecutor standardThreadExecutor() {
        return new ThreadPoolTaskExecutor();
    }

    @Bean("threadPoolTaskExecutor")
    public org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor threadPool = new org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor();
        threadPool.setCorePoolSize(2);
        threadPool.setMaxPoolSize(2);
        threadPool.setQueueCapacity(queueCapacity);
        threadPool.setThreadNamePrefix("serialize-");
        threadPool.initialize();
        return threadPool;
    }

}
