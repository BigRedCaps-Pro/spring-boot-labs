package com.we.javaapi.confirm;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.we.javaapi.util.ResourceUtil;


/**
 * 消息发送confirm模式--普通确认：每发送一条消息后，调用channel.waitForConfirms()方法，同步等待服务器端confirm。实际上
 * 是一种串行confirm。
 * @author we
 * @date 2021-07-14 16:03
 **/
public class NormalConfirmProducer {
    public static final String QUEUE_NAME = "ORIGIN_QUEUE";

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUri(ResourceUtil.getKey("rabbitmq.uri"));

        Connection conn = factory.newConnection();
        Channel channel = conn.createChannel();

        String msg = "Hello world,Rabbit MQ,Normal Confirm";
        // 声明队列（默认交换机AMQP default,Direct）
        // String queue,Boolean durable,boolean exclusive,boolean autoDelete,Map<String,Object> map
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);

        // 开启发送方确认模式
        channel.confirmSelect();

        channel.basicPublish("", QUEUE_NAME, null, msg.getBytes());
        // 普通Confirm，发送一条，确认一条
        if (channel.waitForConfirms()) {
            System.out.println("消息发送成功" );
        }

        channel.close();
        conn.close();

    }
}
