package com.sewage.springboot;

import com.sewage.springboot.graph.GraphUpdate;
import com.sewage.springboot.timer.task.WriteRecordTask;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Timer;


@SpringBootApplication
@MapperScan("com.sewage.springboot.dao")
@tk.mybatis.spring.annotation.MapperScan("com.sewage.springboot.mapper.impl")
@EnableTransactionManagement
public class AiSewageSystemApplication extends SpringBootServletInitializer {
	public static void main(String[] args) {
		SpringApplication application = new SpringApplication(AiSewageSystemApplication.class);
		application.setBannerMode(Banner.Mode.OFF);
		SpringApplication.run(AiSewageSystemApplication.class, args);
		// 半小时做一次图谱数据监测
		Timer timer = new Timer();
//		timer.schedule(new GraphUpdate(), 0, 1800 * 1000);
		// 1小时记录一次数据
		timer.schedule(new WriteRecordTask(), 0, 3600 * 1000);
	}
}