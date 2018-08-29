package com.moka.job.config.mybatis;

import tk.mybatis.mapper.common.ConditionMapper;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * 
 * @author lintian
 * @since 2017-5-8 11:10
 * Mapper一般业务的通用接口
 */
public interface MyMapper<T> extends Mapper<T>,MySqlMapper<T> ,ConditionMapper<T>{

}
