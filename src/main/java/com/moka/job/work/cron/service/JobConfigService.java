package com.moka.job.work.cron.service;

import java.util.List;

import com.moka.job.work.cron.model.po.JobConfig;

public interface JobConfigService {
	/**
	 * 通过定时任务ID查询定时任务信息
	 * @param jobId
	 * @return
	 */
	JobConfig getJobConfig(String jobId);
	/**
	 * 更新或添加配置文件
	 * @param jobConfig
	 * @return
	 */
	Boolean saveOrUpdate(JobConfig jobConfig);
	List<JobConfig> query();
}
