package com.moka.job.config.batch;

import org.apache.ibatis.session.SqlSessionFactory;


public interface SqlSessionTemplateProvider {


    SqlSessionFactory sqlSessionFactory();
}
