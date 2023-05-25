package com.ll.gramgram.boundedContext.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import java.util.concurrent.Executor;

@Configuration
public class CustomConfig {

    @Bean
    public Executor asyncThreadPool() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setMaxPoolSize(4);
        executor.setKeepAliveSeconds(30);
        executor.setCorePoolSize(4);
        executor.setThreadNamePrefix("custom-");
        executor.setQueueCapacity(30);
        return executor;
    }
}
