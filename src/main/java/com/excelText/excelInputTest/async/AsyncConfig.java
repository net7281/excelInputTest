package com.excelText.excelInputTest.async;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

//비동기 처리 환경설정
@EnableAsync
@Configuration
public class AsyncConfig {
	
	@Bean
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5); //기본 스레드 수 
        executor.setMaxPoolSize(30); // 최대 스레드 수
        executor.setQueueCapacity(50); // Queue 사이즈
        executor.setThreadNamePrefix("ASYNC-"); //스레드 접두사 지정
        executor.initialize();
        return executor;
    }
}
