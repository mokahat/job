package com.moka.job.config.batch;

import org.apache.ibatis.session.SqlSessionFactory;


public class SqlSessionTemplateProviderImpl implements SqlSessionTemplateProvider {

    private SqlSessionFactory sqlSessionFactory;

    @Override
    public SqlSessionFactory sqlSessionFactory() {
        return sqlSessionFactory;
    }

    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

}