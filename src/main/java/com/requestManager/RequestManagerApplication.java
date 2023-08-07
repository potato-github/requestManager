package com.requestManager;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@ConfigurationPropertiesScan("com.requestManager")
@SpringBootApplication
@EnableAspectJAutoProxy
@MapperScan("com.requestManager.mapper")
public class RequestManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(RequestManagerApplication.class, args);
	}

}
