package com.infinitysolutions.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cloud.netflix.eureka.EurekaClientConfigBean;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class EurekaTestConfig {
    @Bean
    public EurekaClientConfigBean eurekaClientConfigBean() {
        EurekaClientConfigBean bean = new EurekaClientConfigBean();
        bean.setEnabled(false);
        return bean;
    }
}
