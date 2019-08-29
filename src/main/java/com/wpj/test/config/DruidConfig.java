package com.wpj.test.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class DruidConfig {

    /**
     * 将所有前缀为spring.datasource下的配置项都加载到DataSource中
     * @return
     */
//    @ConfigurationProperties(prefix = "spring.datasource")
//    @Bean
//    public DataSource druidDataSource() {
//        return new DruidDataSource();
//    }
}
