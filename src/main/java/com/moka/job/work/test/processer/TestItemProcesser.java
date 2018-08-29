package com.moka.job.work.test.processer;

import org.springframework.batch.item.ItemProcessor;

import com.moka.job.work.test.model.po.TestData;

public class TestItemProcesser implements ItemProcessor<TestData, TestData>{

	@Override
	public TestData process(TestData item) throws Exception {
		// TODO Auto-generated method stub
		return item;
	}

}
