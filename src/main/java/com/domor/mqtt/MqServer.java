package com.domor.mqtt;

import com.alibaba.fastjson.JSON;
import com.domor.config.MqttConfig;
import com.domor.domain.PubMessage;
import com.domor.utils.CommonUtil;
import com.domor.utils.SpringUtils;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

@Slf4j
public class MqServer {

    private MqttConfig config = SpringUtils.getBean("mqttConfig");

    private MqttClient client;
    private MqttConnectOptions options;

    private String TOPIC = "Fishing/para/";
    private int qos = 2;

    public MqServer() {
        try {
            log.info(">>>>>>>>>>>>>>>>>>>>mqtt-pub：初始化客户端，建立连接");
            String clientId = "mqttPubClient" + CommonUtil.getRandomString(8);
            // 初始化mqtt客户端
            client = new MqttClient(config.getHost(), clientId, new MemoryPersistence());
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
            client.connect(options);
            log.info(">>>>>>>>>>>>>>>>>>>>mqtt-pub：服务器连接成功！");
        } catch (Exception e) {
            log.error(">>>>>>>>>>>>>>>>>>>>mqtt-pub：初始化客户端失败！");
            e.printStackTrace();
        }
    }

    /**
     * 发布消息
     * @param devTopic 设备的topic
     * @param pm 消息JSON字符串
     */
    public void publish(String devTopic, PubMessage pm) {
        try {
            String topic = TOPIC + devTopic;
            String contnent = JSON.toJSONString(pm);
            log.info(">>>>>>>>>>>>>>>>>>>>mqtt-pub：消息发布内容："+topic+":"+contnent);
            MqttMessage message = new MqttMessage();
            message.setQos(qos);
            message.setRetained(false);
            message.setPayload(contnent.getBytes());
            MqttDeliveryToken token = client.getTopic(topic).publish(message);
            token.waitForCompletion();
            log.info(">>>>>>>>>>>>>>>>>>>>mqtt-pub：消息发布成功！");
        } catch (MqttException e) {
            log.error(">>>>>>>>>>>>>>>>>>>>mqtt-pub：消息发布失败！");
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
