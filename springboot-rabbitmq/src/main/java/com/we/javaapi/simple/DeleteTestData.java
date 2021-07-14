package com.we.javaapi.simple;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 删除所有测试数据
 * @author we
 * @date 2021-07-13 11:41
 **/
public class DeleteTestData {
    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        // 连接IP
        factory.setHost("127.0.0.1");
        // 连接端口
        factory.setPort(5672);
        // 虚拟机
        factory.setVirtualHost("/");
        // 用户
        factory.setUsername("guest");
        factory.setPassword("guest");

        // 建立连接
        Connection conn = factory.newConnection();
        // 创建消息通道
        Channel channel = conn.createChannel();

        String[] queueNames = {"DELAY_QUEUE","GP_FIRST_QUEUE", "GP_FOURTH_QUEUE", "GP_SECOND_QUEUE", "GP_THIRD_QUEUE",
                "MY_FIRST_QUEUE", "MY_FOURTH_QUEUE", "MY_SECOND_QUEUE", "MY_THIRD_QUEUE", "SIMPLE_QUEUE",
                "DLX_QUEUE","TEST_DLX_QUEUE","TEST_TTL_QUEUE"};


        String[] exchangeNames = {"DELAY_EXCHANGE","GP_DIRECT_EXCHANGE","DLX_EXCHANGE","GP_FANOUT_EXCHANGE", "GP_TOPIC_EXCHANGE",
                "MY_DIRECT_EXCHANGE", "MY_FANOUT_EXCHANGE", "MY_TOPIC_EXCHANGE", "SIMPLE_EXCHANGE"};

        for(String queueName : queueNames){
            channel.queueDelete(queueName);
        }

        for(String exchangeName : exchangeNames){
            channel.exchangeDelete(exchangeName);
        }

        channel.close();
        conn.close();

    }
}
