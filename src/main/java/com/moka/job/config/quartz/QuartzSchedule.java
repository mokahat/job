package com.moka.job.config.quartz;

import org.quartz.Scheduler;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class QuartzSchedule {
	@Autowired
    private MyJobFactory myJobFactory;
	@Bean
    public Scheduler schedulerFactoryBean() throws Exception {
    	Scheduler schedulerFactory = StdSchedulerFactory.getDefaultScheduler();
    	//registerJobAndTrigger(schedulerFactory);
    	 // 自定义Job Factory，用于Spring注入
    	schedulerFactory.setJobFactory(myJobFactory);
        return schedulerFactory;
    }
}
