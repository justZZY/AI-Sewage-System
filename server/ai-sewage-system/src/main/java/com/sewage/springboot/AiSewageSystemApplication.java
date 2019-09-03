package com.sewage.springboot;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;


@SpringBootApplication
@MapperScan("com.sewage.springboot.dao")
public class AiSewageSystemApplication extends SpringBootServletInitializer {
	public static void main(String[] args) {
		SpringApplication application = new SpringApplication(AiSewageSystemApplication.class);
		application.setBannerMode(Banner.Mode.OFF);
		SpringApplication.run(AiSewageSystemApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		// 注意这里要指向原先用main方法执行的Application启动类
		return builder.sources(AiSewageSystemApplication.class);
	}
}