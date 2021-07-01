package com.app.conf;

import java.time.Duration;

import javax.sql.DataSource;

import org.apache.ibatis.session.ExecutorType;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.client.RestTemplate;

@Configuration
public class DatabaseConfiguration {
	@Autowired
	private Environment env;
	
	
	@Bean
	public SqlSessionFactoryBean sqlSessionFactoryBean() {
	    SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
	    sqlSessionFactoryBean.setDataSource(dataSource());
	    sqlSessionFactoryBean.setConfigLocation(new ClassPathResource("sqlmapconfig.xml"));
	    
	    return sqlSessionFactoryBean;
	}
	
	
	@Bean
	public DataSource dataSource() {
		// TODO Auto-generated method stub
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(env.getProperty("spring.datasource.driverClassName"));
		dataSource.setUrl(env.getProperty("spring.datasource.url"));
		dataSource.setUsername(env.getProperty("spring.datasource.username"));
		dataSource.setPassword(env.getProperty("spring.datasource.password"));
		return dataSource;
	}



	  @Bean
	  public SqlSessionTemplate sqlSession(SqlSessionFactoryBean sqlSessionFactoryBean) throws Exception {
	    return new SqlSessionTemplate(sqlSessionFactoryBean.getObject(),ExecutorType.BATCH);
	  }

}
