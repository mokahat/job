package com.moka.job.config.quartz;

import java.util.List;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.moka.job.work.cron.mapper.JobConfigMapper;
import com.moka.job.work.cron.model.po.JobConfig;
/**
 * 动态任务用户调整Spring的任务
 * @author lintian
 * @since 2018-08-25 10:02
 */
@Component
public class DynamicTaskCompoent {
	private static final Logger logger = LoggerFactory.getLogger(DynamicTaskCompoent.class);
	

    @Autowired
	private Scheduler schedulerFactory;
    @Autowired
    private JobConfigMapper jobConfigMapper;
	
	/**
	 * 更新定时任务的触发表达式
	 * 
	 * @param triggerName
	 *            触发器名字
	 * @param start
	 *            触发表达式
	 * @return 成功则返回true，否则返回false
	 */
	public boolean startOrStop(String triggerName,
			boolean start) {
		
		//Scheduler schedulerFactory = schedulerFactoryBean.getScheduler();
		try {
			TriggerKey triggerKey = new TriggerKey(triggerName);
			if(start){
				schedulerFactory.resumeTrigger(triggerKey);
				logger.info("trigger the start successfully!!");
			}else{
				schedulerFactory.pauseTrigger(triggerKey);
				logger.info("trigger the pause successfully!!");
			}
			return true;
		}  catch (SchedulerException e) {
			logger.error("Fail to reschedule. " + e);
			return false;
		}
	}
	
	/**
	 * 更新定时任务的触发表达式
	 * 
	 * @param triggerName
	 *            触发器名字
	 * @param cronExpression
	 *            触发表达式
	 * @return 成功则返回true，否则返回false
	 */
	public boolean updateCronExpression(String triggerName,
			String cronExpression) {
		//Scheduler schedulerFactory = schedulerFactoryBean.getScheduler();
		try {
			TriggerKey triggerKey = new TriggerKey(triggerName);
			CronTrigger trigger = (CronTrigger) getTrigger(triggerName,
					Scheduler.DEFAULT_GROUP);
			if (trigger == null) {
				return false;
			}
			if (trigger.getCronExpression().equals( cronExpression)) {
				logger.info("cronExpression is same with the running Schedule , no need to update.");
				return true;
			}
			//将cron表达式进行转换
            CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);
            Trigger newtrigger = TriggerBuilder.newTrigger().withIdentity(triggerKey).withSchedule(cronScheduleBuilder).build();
			schedulerFactory.rescheduleJob(triggerKey,
					newtrigger);
			
			logger.info("Update the cronExpression successfully!!");
			return true;
		
		} catch (SchedulerException e) {
			logger.error("Fail to reschedule. " + e);
			return false;
		}
	}
	/**
	 * 获取触发器
	 * 
	 * @param triggerName
	 *            触发器名字
	 * @param groupName
	 *            触发器组名字
	 * @return 对应Trigger
	 */
	private Trigger getTrigger(String triggerName, String groupName) {
		//Scheduler schedulerFactory = schedulerFactoryBean.getScheduler();
		Trigger trigger = null;
		if (StringUtils.isEmpty(groupName)) {
			logger.warn("Schedule Job Group is empty!");
			return null;
		}
		if (StringUtils.isEmpty(triggerName)) {
			logger.warn("Schedule trigger Name is empty!");
			return null;
		}
		try {
			TriggerKey  key = new TriggerKey(triggerName,groupName);
			trigger = schedulerFactory.getTrigger(key);
			List<String> a =schedulerFactory.getTriggerGroupNames();
			System.out.println(a);
		} catch (SchedulerException e) {
			logger.warn("Fail to get the trigger (triggerName: " + triggerName
					+ ", groupName : " + groupName + ")");
			return null;
		}
		if (trigger == null) {
			logger.warn("Can not found the trigger of triggerName: "
					+ triggerName + ", groupName : " + groupName);
		}
		return trigger;
	}
	
    
    
    /**
     * 注册一个任务和触发器
     * @throws ClassNotFoundException 
     */
    public  void registerJobAndTrigger() throws ClassNotFoundException {
    	// 开启调度器
        try {
        	schedulerFactory.start();
		} catch (SchedulerException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        
    	List<JobConfig> jobConfigs = jobConfigMapper.selectAll();
    	for(JobConfig jobConfig : jobConfigs){
    		 JobDetail job = JobBuilder.newJob((Class<? extends Job>) Class.forName(jobConfig.getJobName()))
    	                .withIdentity(jobConfig.getJobName())
    	                .build();
    		     //将cron表达式进行转换
                CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(jobConfig.getCron());
    	        Trigger trigger = org.quartz.TriggerBuilder.newTrigger()
    	                .withIdentity(jobConfig.getJobName())
    	                .withSchedule(cronScheduleBuilder)
    	                .build();

    	        try {
    	        	
    	        	schedulerFactory.scheduleJob(job,trigger);
    	        } catch (SchedulerException e) {
    	            logger.error("注册任务和触发器失败", e);
    	        }
    	}
       
    }
}
