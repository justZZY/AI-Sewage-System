package com.sewage.springboot.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import com.sewage.springboot.service.JobService;

@Configuration 
public class JobScheduleTask {
	
	@Autowired JobService jobService;

	@Scheduled(cron = "0 */5 * * * ?")
	public void autoAllocate() {
		System.out.println("五分自动扫描派单一次");
		jobService.autoAllocate();
	}
	


}
