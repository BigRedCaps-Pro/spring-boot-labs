package com.we.javaapi.limit;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.we.javaapi.util.ResourceUtil;

/**
 * 消息生产者，在启动消费者之后，在启动
 * 用于测试消费者限流
 * @author we
 * @date 2021-07-14 10:53
 **/
public class LimitProducer {
    private final static String QUEUE_NAME = "TEST_LIMIT_QUEUE";

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUri(ResourceUtil.getKey("rabbitmq.uri"));

        // 建立连接
        Connection conn = factory.newConnection();
        // 创建通道
        Channel channel = conn.createChannel();

        String msg = "a limit message";
        // 声明队列（默认交换机AMQP default，Direct）
        // String queue, boolean durable, boolean exclusive, boolean autoDelete, Map<String, Object> arguments
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        // 发送消息
        // String exchange, String routingKey, BasicProperties props, byte[] body
        for(int i=0; i<100; i++){
            channel.basicPublish("", QUEUE_NAME, null, (msg+i).getBytes());
        }

        channel.close();
        conn.close();

    }
}
