package com.sewage.springboot.task;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

//@Configuration 
public class JobScheduleTask {

	@Scheduled(cron = "0 */5 * * * ?")
	public void autoAllocate() {
		System.out.println("五分自动扫描派单一次");
	}
	


}
