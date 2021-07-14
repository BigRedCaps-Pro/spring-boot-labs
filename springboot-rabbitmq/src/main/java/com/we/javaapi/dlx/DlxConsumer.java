package com.we.javaapi.dlx;

import com.rabbitmq.client.*;
import com.we.javaapi.util.ResourceUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/**
 * 消息消费者，由于消费的代码被注释掉了，10s后，消息会从正常队列TEST_DLX_QUEUE 到达
 * DLX_EXCHANGE,然后路由到死信队列DLX_QUEUE
 * @author we
 * @date 2021-07-13 14:06
 **/
public class DlxConsumer {
    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUri(ResourceUtil.getKey("rabbitmq.uri"));

        // 建立连接
        Connection conn = factory.newConnection();
        // 创建消息通道
        Channel channel = conn.createChannel();

        // 指定队列的死信交换机
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange","DLX_EXCHANGE");
        // 设置队列的TTL
        //arguments.put("x-expires","9000");
        // 如果设置了队列的最大长度，超过长度是，先入队的消息会被发送到DLX
        //arguments.put("x-max-length",4);

        // 声明对列（默认交换机AMQP default,Direct）
        // String queue,boolean durable,boolean exclusive,boolean autoDelete,Map<String,Object> arguments
        channel.queueDeclare("TEST_DLX_QUEUE",false,false,false,arguments);

        // 声明死信交换机
        channel.exchangeDeclare("DLX_EXCHANGE","topic",false,false,false,null);
        // 声明死信队列
        channel.queueDeclare("DLX_QUEUE",false,false,false,null);
        // 绑定,此处 Dead letter routing key 设置为 #
        channel.queueBind("DLX_QUEUE","DLX_EXCHANGE","#");
        System.out.println(" Waiting for message....");

        // 创建消费者
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
                                       byte[] body) throws IOException {
                String msg = new String(body, "UTF-8");
                System.out.println("Received message : '" + msg + "'");
            }
        };

        // 开始获取消息
        // String queue, boolean autoAck, Consumer callback
        //channel.basicConsume("TEST_DLX_QUEUE", true, consumer);

    }
}
