package com.we.javaapi.limit;

import com.rabbitmq.client.*;
import com.we.javaapi.util.ResourceUtil;

import java.io.IOException;

/**
 * 消息消费者，测试消费端限流，先启动
 * @author we
 * @date 2021-07-14 10:57
 **/
public class LimitConsumer {

    public static final String QUEUE_NAME = "TEST_LIMIT_QUEUE";

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUri(ResourceUtil.getKey("rabbitmq.uri"));

        // 建立连接
        Connection conn = factory.newConnection();
        // 创建消息通道
        final Channel channel = conn.createChannel();

        // 声明队列（默认交换机AMQP default，Direct）
        // String queue, boolean durable, boolean exclusive, boolean autoDelete, Map<String, Object> arguments
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        System.out.println("Consumer1  Waiting for message....");

        // 创建消费者，并接收消息
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
                                       byte[] body) throws IOException {
                String msg = new String(body, "UTF-8");

                System.out.println("Consumer1 Received message : '" + msg + "'" );

                channel.basicAck(envelope.getDeliveryTag(), true);

            }
        };

        // 非自动确认消息的前提下，如果一定数目的消息（通过基于consume或者channel设置Qos的值）未被确认前，不在进行消费新消息
        channel.basicQos(2);
        channel.basicConsume(QUEUE_NAME,false,consumer);
    }

}
