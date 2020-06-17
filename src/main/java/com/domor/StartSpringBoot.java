package com.domor;

import com.domor.mqtt.MqClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@Slf4j
@EnableCaching
@EnableScheduling
@SpringBootApplication
public class StartSpringBoot {

    public static MqClient mqClient;

    public static void main(String[] args) {
        SpringApplication.run(StartSpringBoot.class, args);
        // 初始化mq客户端并批量订阅主题
        mqClient = new MqClient();
    }

}
