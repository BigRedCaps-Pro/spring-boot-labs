package com.we.javaapi.dlx;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.we.javaapi.util.ResourceUtil;


/**
 * 消息生产者，通过TTL测试死信队列
 * @author we
 * @date 2021-07-13 11:31
 **/
public class DlxProducer {
    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUri(ResourceUtil.getKey("rabbitmq.uri"));

        // 建立连接
        Connection conn = factory.newConnection();
        // 创建消息通道
        Channel channel = conn.createChannel();

        String msg = "Hello World,RabbitMQ ,DLX MSG";

        // 设置属性，消息10s过期
        AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder()
                .deliveryMode(2)// 持久化消息
                .contentEncoding("UTF-8")
                .expiration("10000")// TTL
                .build();

        // 发送消息
        channel.basicPublish("","TEST_DLX_QUEUE",properties,msg.getBytes());

        channel.close();
        conn.close();

    }

}
