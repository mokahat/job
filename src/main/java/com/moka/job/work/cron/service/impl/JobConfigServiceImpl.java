package com.moka.job.work.cron.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.moka.job.work.cron.mapper.JobConfigMapper;
import com.moka.job.work.cron.model.po.JobConfig;
import com.moka.job.work.cron.service.JobConfigService;

@Service
public class JobConfigServiceImpl implements JobConfigService{
	private final static Logger logger = LoggerFactory.getLogger(JobConfigServiceImpl.class);
	
	@Autowired
	private JobConfigMapper jobConfigMapper;
	@Override
	public JobConfig getJobConfig(String jobId) {
		
		return jobConfigMapper.selectByPrimaryKey(jobId);
	}
	@Override
	public Boolean saveOrUpdate(JobConfig jobConfig) {
		int effCnt = 0;
		if(StringUtils.isEmpty(jobConfig.getJobId())){
			effCnt = jobConfigMapper.insertSelective(jobConfig);
		}else{
			effCnt = jobConfigMapper.deleteByPrimaryKey(jobConfig);
		}
		return effCnt > 0;
	}
	@Override
	public List<JobConfig> query() {
	
		return jobConfigMapper.selectAll();
	}
}
