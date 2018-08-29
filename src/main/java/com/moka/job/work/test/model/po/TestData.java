package com.moka.job.work.test.model.po;

import javax.persistence.Id;
import javax.persistence.Table;

@Table(name="job_test_data")
public class TestData {
	@Id
	private int id;
	private int updateId;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUpdateId() {
		return updateId;
	}
	public void setUpdateId(int updateId) {
		this.updateId = updateId;
	}
	
}
