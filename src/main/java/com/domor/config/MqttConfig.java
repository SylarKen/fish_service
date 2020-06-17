package com.domor.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @desc: mqtt配置参数类
 * @auther: liyuyang
 * @date: 2019/12/18
 **/
@Data
@Component
@ConfigurationProperties(prefix = "mqtt")
public class MqttConfig {

    private String host;
    private String username;
    private String password;
    private String clientId;
    private Integer interval;
    private Integer timeout;
    private boolean clearSession;

    public boolean getClearSession(){
        return clearSession;
    }

}
