package com.sewage.springboot.task;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
@EnableScheduling
public class ScheduleConfiguration {
	
	/**
	 * 使用 websockt注解的时候，使用@EnableScheduling注解
	 * 启动的时候一直报错，增加这个bean 则报错解决。
	 * 报错信息：  Unexpected use of scheduler.
	 */
	@Bean
	public TaskScheduler taskScheduler(){
		ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
		taskScheduler.setPoolSize(10);
		taskScheduler.initialize();
		return taskScheduler;
	}
}
