package com.moka.job.config.mybatis;

import java.util.Properties;

import javax.sql.DataSource;

import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.autoproxy.BeanNameAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;
import org.springframework.transaction.interceptor.TransactionInterceptor;

import com.alibaba.druid.pool.DruidDataSource;
import com.github.pagehelper.PageHelper;

@Configuration
@EnableTransactionManagement
public class MybatisConfig implements TransactionManagementConfigurer{
	private static Logger logger = LoggerFactory.getLogger(MybatisConfig.class);
	@Autowired
	DataSource dataSource;

	@Bean(name = "sqlSessionFactory")
	public SqlSessionFactory sqlSessionFactoryBean()
	{
		SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
		bean.setDataSource(getDataSource());
		//bean.setTypeAliasesPackage("com.bqjr.mall");

		// 添加XML目录
		ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		try
		{
			bean.setMapperLocations(resolver.getResources("classpath*:/mapper/*.xml"));
			Interceptor[] plugins =  new Interceptor[]{pageHelper()};
		    bean.setPlugins(plugins);
			return bean.getObject();
		} catch (Exception e){
			logger.error("SqlSessionFactory 注入失败",e);
			throw new RuntimeException(e);
		}
		 
	}
	/**
     * 换成阿里数据源
     * @return
     */
    @Bean
    @Primary
    @ConfigurationProperties(prefix="spring.datasource")
    public DataSource getDataSource(){
    	return new DruidDataSource();
    }

   /**
     *  分页插件
     *	@author lintian
     *  @since 2017-5-8 12:38
     */
    @Bean
    public PageHelper pageHelper() {
    	logger.debug("注册MyBatis分页插件PageHelper");
        PageHelper pageHelper = new PageHelper();
        Properties p = new Properties();
        p.setProperty("offsetAsPageNum", "true");
        p.setProperty("rowBoundsWithCount", "true");
        p.setProperty("reasonable", "true");
        pageHelper.setProperties(p);
        return pageHelper;
    }
	@Bean
	public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory)
	{
		return new SqlSessionTemplate(sqlSessionFactory);
	}

	@Bean
	@Override
	@Primary
	public PlatformTransactionManager annotationDrivenTransactionManager()
	{
		return new DataSourceTransactionManager(dataSource);
	}

	
	//platformTransactionManager 为springboot默认初始化好的对象，，无需定义
	@Bean(name = "transactionInterceptor")
	public TransactionInterceptor transactionInterceptor(
			PlatformTransactionManager platformTransactionManager) {
		TransactionInterceptor transactionInterceptor = new TransactionInterceptor();
		// 事物管理器
		transactionInterceptor.setTransactionManager(platformTransactionManager);
		Properties transactionAttributes = new Properties();
		// 新增
		transactionAttributes.setProperty("insert*","PROPAGATION_REQUIRED,-Throwable");
		// 修改
		transactionAttributes.setProperty("update*","PROPAGATION_REQUIRED,-Throwable");
		// 删除
		transactionAttributes.setProperty("delete*","PROPAGATION_REQUIRED,-Throwable");
		//查询
		transactionAttributes.setProperty("select*","PROPAGATION_REQUIRED,-Throwable");
		//查询
		transactionAttributes.setProperty("get*","PROPAGATION_REQUIRED,-Throwable");
		//业务处理
		transactionAttributes.setProperty("deal*","PROPAGATION_REQUIRED,-Throwable");

		transactionInterceptor.setTransactionAttributes(transactionAttributes);
		return transactionInterceptor;
	}
	//代理到ServiceImpl的Bean
	@Bean
	public BeanNameAutoProxyCreator transactionAutoProxy() {
		BeanNameAutoProxyCreator transactionAutoProxy = new BeanNameAutoProxyCreator();
		transactionAutoProxy.setProxyTargetClass(true);
		transactionAutoProxy.setBeanNames("*ServiceImpl");
		transactionAutoProxy.setInterceptorNames("transactionInterceptor");
		return transactionAutoProxy;
	}
}
