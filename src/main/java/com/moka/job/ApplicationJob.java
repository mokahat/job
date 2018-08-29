package com.moka.job;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@PropertySource(value={"classpath*:alone/*.properties","classpath:alone/*.yml"},encoding="utf-8",ignoreResourceNotFound=true)
public class ApplicationJob {
	public static void main(String[] args) {
		SpringApplication.run(ApplicationJob.class, args);
	}
}