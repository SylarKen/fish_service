package com.domor.mqtt;

import com.domor.config.MqttConfig;
import com.domor.utils.SpringUtils;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

/**
* @Desc: mqtt客户端连接类，负责与服务器建立连接并订阅主题
* @Author: liyuyang
* @Date: 2019/12/11
**/
@Slf4j
public class MqClient {

    private MqttConfig config = SpringUtils.getBean("mqttConfig");

    private MqttClient client;
    private MqttConnectOptions options;

    private String[] topics = {"Fishing/data/+", "Fishing/status/+"};
    private int[] qos = {0, 2};

    public MqClient() {
        try {
            log.info(">>>>>>>>>>>>>>>>>>>>mqtt-sub:初始化客户端，建立连接,订阅主题");

            // 初始化mqtt客户端
            client = new MqttClient(config.getHost(), config.getClientId(), new MemoryPersistence());
            client.setCallback(new MqCallback());
            // 设置连接参数
            options = new MqttConnectOptions();
            // 设置是否清空session,这里如果设置为false表示服务器会保留客户端的连接记录，这里设置为true表示每次连接到服务器都以新的身份连接
            options.setCleanSession(config.getClearSession());
            // 设置连接的用户名
            options.setUserName(config.getUsername());
            // 设置连接的密码
            options.setPassword(config.getPassword().toCharArray());
            // 设置超时时间 单位为秒
            options.setConnectionTimeout(config.getTimeout());
            // 设置会话心跳时间 单位为秒
            options.setKeepAliveInterval(config.getInterval());

            // 建立连接
            start();
            log.info(">>>>>>>>>>>>>>>>>>>>mqtt-sub:服务器连接成功！");
            // 订阅主题
            subscribe();
            log.info(">>>>>>>>>>>>>>>>>>>>mqtt-sub:订阅主题成功！");

        } catch (Exception e) {
            log.error(">>>>>>>>>>>>>>>>>>>>mqtt-sub:初始化客户端失败！");
            e.printStackTrace();
        }
    }

    /**
     * 与mqtt服务连接
     */
    private void start(){
        try {
            client.connect(options);
        } catch (Exception e1) {
            log.error(">>>>>>>>>>>>>>>>>>>>mqtt-sub:服务器连接失败！");
            e1.printStackTrace();
            reConnect();
        }
    }


    /**
     * 与mqtt服务重连
     */
    public void reConnect() {
        while (true) {
            try {
                if (!client.isConnected()) {
                    Thread.sleep(20000);
                    log.info(">>>>>>>>>>>>>>>>>>>>mqtt-sub:正在尝试重连。。。");
                    client.connect(options);
                } else {
                    break;
                }
            } catch (Exception e) {
                log.error(">>>>>>>>>>>>>>>>>>>>mqtt-sub:服务器连接失败！");
                e.printStackTrace();
            }
        }
    }

    /**
     * 订阅消息
     */
    public void subscribe(){
        try {
            client.subscribe(topics, qos);
        } catch (MqttException e) {
            log.error(">>>>>>>>>>>>>>>>>>>>mqtt-sub:订阅主题失败！");
            e.printStackTrace();
        }
    }


    /**
     * 断开连接，关闭客户端
     */
    public void stop() {
        try {
            client.disconnect();
            client.close();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

}
