package com.sewage.springboot;

import com.sewage.springboot.logger.ConsoleLoggerFactory;
import com.sewage.springboot.signalr.FBoxSignalRConnection;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AiSewageSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(AiSewageSystemApplication.class, args);
	}

}

