package com.sewage.springboot;

import com.sewage.springboot.listener.CloseListener;
import com.sewage.springboot.listener.StartListener;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.transaction.annotation.EnableTransactionManagement;



@SpringBootApplication
@MapperScan("com.sewage.springboot.dao")
@tk.mybatis.spring.annotation.MapperScan("com.sewage.springboot.mapper.impl")
@EnableTransactionManagement
public class AiSewageSystemApplication extends SpringBootServletInitializer {

	protected SpringApplicationBuilder configure(SpringApplicationBuilder applicationBuilder) {
		return applicationBuilder.sources(AiSewageSystemApplication.class);
	}

	public static void main(String[] args) {
		SpringApplication application = new SpringApplication(AiSewageSystemApplication.class);
		application.setBannerMode(Banner.Mode.OFF);
		application.addListeners(new StartListener());
		application.addListeners(new CloseListener());
		SpringApplication.run(AiSewageSystemApplication.class, args);
	}
}