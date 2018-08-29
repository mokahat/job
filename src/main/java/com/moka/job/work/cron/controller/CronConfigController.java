package com.moka.job.work.cron.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.moka.job.config.batch.BatchConfig;
import com.moka.job.config.quartz.DynamicTaskCompoent;
import com.moka.job.work.cron.mapper.JobConfigMapper;
import com.moka.job.work.cron.model.po.JobConfig;
import com.moka.job.work.cron.service.JobConfigService;

/**
 * 跑批表达式添加控制器
 * @author lintian
 * @since 2018-08-24 17:21
 */
@RestController
@RequestMapping("/cron/config")
public class CronConfigController {
	private static final Logger logger = LoggerFactory.getLogger(CronConfigController.class);
	@Autowired
	private JobConfigService jobConfigService;
	@Autowired
	private BatchConfig batchConfig;
	@Autowired
    private JobConfigMapper jobConfigMapper;
	@Autowired
    private DynamicTaskCompoent dynamicTaskCompoent;
	
	@RequestMapping(value = "/saveOrUpdate" , method = RequestMethod.POST)
	public Object saveOrUpdate(@RequestBody JobConfig jobConfig){
		Boolean result = jobConfigService.saveOrUpdate(jobConfig);
		return result;
	}
	@RequestMapping(value = "/query" , method = RequestMethod.GET)
	public Object query(){
		List<JobConfig> list = jobConfigService.query();
		return list;
	}
	@RequestMapping(value = "/execute" , method = RequestMethod.POST)
	public Object execute(String jobId){
		
		String name = jobId.substring(jobId.lastIndexOf(".")+1,jobId.length());
		JobConfig config = jobConfigMapper.selectByPrimaryKey(name);
		dynamicTaskCompoent.updateCronExpression(jobId, config.getCron());
		
		return "进行执行中。。。";
	}
	@RequestMapping(value = "/init" , method = RequestMethod.POST)
	public Object init(String jobId) throws ClassNotFoundException{
		if(null == batchConfig){
			System.out.println("batchConfig is null.....");
		}
		String name = jobId.substring(jobId.lastIndexOf(".")+1,jobId.length());
		JobConfig config = jobConfigMapper.selectByPrimaryKey(name);
		dynamicTaskCompoent.registerJobAndTrigger();
		
		return "进行执行中。。。";
	}
	@RequestMapping(value = "/stop" , method = RequestMethod.POST)
	public Object stop(String jobId) throws ClassNotFoundException{
		
		
		
		dynamicTaskCompoent.startOrStop(jobId, false);
		
		return "进行执行中。。。";
	}
	
	@RequestMapping(value = "/start" , method = RequestMethod.POST)
	public Object start(String jobId) throws ClassNotFoundException{
		
		
		
		dynamicTaskCompoent.startOrStop(jobId, false);
		
		return "进行执行中。。。";
	}
	
}
