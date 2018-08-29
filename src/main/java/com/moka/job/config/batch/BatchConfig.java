package com.moka.job.config.batch;

import java.util.Date;

import org.apache.ibatis.session.SqlSessionFactory;
import org.quartz.Job;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobLocator;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;



@Configuration
@EnableBatchProcessing
@EnableScheduling
public class BatchConfig{
    private static final Logger logger = LoggerFactory.getLogger(BatchConfig.class);

    private String groupName;


    @Bean
    public JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor(JobRegistry jobRegistry) {
        JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor =  new JobRegistryBeanPostProcessor();
        jobRegistryBeanPostProcessor.setGroupName(groupName);
        jobRegistryBeanPostProcessor.setJobRegistry(jobRegistry);
        return jobRegistryBeanPostProcessor;
    }
    @Bean
    public SqlSessionTemplateProviderImpl SqlSessionTemplateProviderImpl(SqlSessionFactory sqlSessionFactory) {
        SqlSessionTemplateProviderImpl sqlSessionTemplateProvider = new SqlSessionTemplateProviderImpl();

        sqlSessionTemplateProvider.setSqlSessionFactory(sqlSessionFactory);
        return sqlSessionTemplateProvider;
    }
    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }


    @Autowired
    private JobLocator jobLocator;

    @Autowired
    private JobLauncher jobLauncher;

   
    public void startJob(String jobClassName) {
        JobParametersBuilder builder = new JobParametersBuilder();
        builder.addDate("run date", new Date());

        JobParameters jobParameters = builder.toJobParameters();
        try {
            logger.info("启动定时任务" + jobClassName +"开始");
            JobExecution jobExecution = jobLauncher.run(jobLocator.getJob(jobClassName), jobParameters);
            logger.info("启动定时任务" + jobClassName +"结束");
            if("FAILED".equals(jobExecution.getExitStatus().getExitCode())){
    			logger.error("{}：定时任务有问题，错误信息{}",jobExecution.getJobInstance().getJobName(),jobExecution.getExitStatus().getExitDescription());
    		
    		}
        } catch (JobExecutionException e) {
            logger.error("启动定时任务" + jobClassName +"失败", e);
        }
    }
 
}
