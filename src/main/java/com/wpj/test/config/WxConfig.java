package com.wpj.test.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author wangpejian
 * @date 19-6-10 下午6:07
 */
@Component
@ConfigurationProperties(prefix = "wx")
@Data
public class WxConfig {

    private String appId;
    private String secret;

}
