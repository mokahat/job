package com.moka.job.base;

import static org.springframework.util.Assert.notNull;
import static org.springframework.util.ClassUtils.getShortName;

import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.batch.item.database.AbstractPagingItemReader;

import com.github.pagehelper.PageHelper;
/**
 * 分页查询
 * @author lintian
 *
 * @param <T>
 */
public class BasePageItemReader<T> extends AbstractPagingItemReader<T>{
	 private String queryId;
	 private Object parameterObject;
	 private SqlSessionFactory sqlSessionFactory;
	 private SqlSessionTemplate sqlSessionTemplate;
	 public BasePageItemReader() {
	        setName(getShortName(BasePageItemReader.class));
	    }
 /**
     * Check mandatory properties.
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();
        notNull(sqlSessionFactory, "sqlSessionFactory 不能为空");
        sqlSessionTemplate = new SqlSessionTemplate(sqlSessionFactory, ExecutorType.BATCH);
        notNull(queryId, "queryId 不能为空");
    }
	@Override
	protected void doReadPage() {
		PageHelper.startPage(getPage(), getPageSize());
	    if (results == null) {
	      results = new CopyOnWriteArrayList<T>();
	    } else {
	      results.clear();
	    }
	    results.addAll(sqlSessionTemplate.<T> selectList(queryId, parameterObject));
		
	}

	@Override
	protected void doJumpToPage(int itemIndex) {
		// TODO Auto-generated method stub
		
	}

	public Object getParameterObject() {
		return parameterObject;
	}

	public void setParameterObject(Object parameterObject) {
		this.parameterObject = parameterObject;
	}

	public String getQueryId() {
		return queryId;
	}

	public void setQueryId(String queryId) {
		this.queryId = queryId;
	}

	public SqlSessionTemplate getSqlSessionTemplate() {
		return sqlSessionTemplate;
	}

	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		this.sqlSessionTemplate = sqlSessionTemplate;
	}
	public SqlSessionFactory getSqlSessionFactory() {
		return sqlSessionFactory;
	}
	public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
		this.sqlSessionFactory = sqlSessionFactory;
	}
	
}
