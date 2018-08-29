package com.moka.job.work.test.writer;

import org.mybatis.spring.SqlSessionTemplate;

import com.moka.job.base.BaseBatchItemWriter;
import com.moka.job.utils.MapperUtils;
import com.moka.job.work.test.mapper.TestDataMapper;
import com.moka.job.work.test.model.po.TestData;

public class TestItemWriter extends BaseBatchItemWriter<TestData>{
	private static final String testDataMapperNamespace = TestDataMapper.class.getName();
	@Override
	protected void doWrite(TestData item) {
		if(null != item){
			item.setUpdateId(item.getId()+1);
			SqlSessionTemplate sqlSessionTemplate = getSqlSessionTemplate();
	        sqlSessionTemplate.update(MapperUtils.statement(TestDataMapper.class.getName(), "updateByPrimaryKeySelective"), item);
		}
		
	}

}
