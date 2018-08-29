package com.moka.job.config.batch;

import java.text.MessageFormat;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerContext;
import org.quartz.SchedulerException;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;


public class JobLauncherDetails implements SqlSessionTemplateProvider, JobStepNameProvider, InitializingBean, Job{
    private String className;

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    private SqlSessionTemplateProviderImpl sqlSessionTemplateProvider;
   
    @Resource
    private BatchConfig batchConfig;

    @Override
    public SqlSessionFactory sqlSessionFactory() {
        return sqlSessionTemplateProvider.sqlSessionFactory();
    }

    @Override
    public String jobName() {
        return className;
    }

    @Override
    public String stepName(int n) {
        return MessageFormat.format("{0}_step_{1}", className, n);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        String className = getClass().getName();
        if (className.contains("$")) {
            this.className = className.substring(0, className.indexOf("$"));
        }
    }

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		
		batchConfig.startJob(context.getTrigger().getKey().getName());
		
	}
    
  /*  @Override
	public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
		String name = jobName().substring(jobName().lastIndexOf(".")+1,jobName().length());
		try{
		taskRegistrar.addTriggerTask(new Runnable() {
            public void run() {
            	System.out.println(name);
            	System.out.println(jobName());
            	batchConfig.startJob(jobName());
            }
        },
			triggerContext -> {JobConfig config = JobConfigMapper.selectByPrimaryKey(name);
			if(null != config){
				return new CronTrigger(config.getCron()).nextExecutionTime(triggerContext);
			}
			return null;
			});
		}catch (Exception e) {
			
		}
		
	}*/
}

