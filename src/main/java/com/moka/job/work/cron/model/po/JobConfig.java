package com.moka.job.work.cron.model.po;

import java.util.Date;

import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "job_config")
public class JobConfig {
	/**
	 * 定时任务ID
	 */
	@Id
	private String jobId;
	/**
	 * 定时任务名
	 */
	private String jobName;
	/**
	 * 定时任务表达式配置
	 */
	private String cron;
	
	private Date createTime;
	private Date updateTime;
	public String getJobId() {
		return jobId;
	}
	public void setJobId(String jobId) {
		this.jobId = jobId;
	}
	public String getJobName() {
		return jobName;
	}
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
	public String getCron() {
		return cron;
	}
	public void setCron(String cron) {
		this.cron = cron;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	
	
	
}
