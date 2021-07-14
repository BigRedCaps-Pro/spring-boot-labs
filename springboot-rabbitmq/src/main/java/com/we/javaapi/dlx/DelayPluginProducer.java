package com.we.javaapi.dlx;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 使用延时插件实现的消息投递-生产者
 * 必须要在服务端安装rabbitmq-delayed-message-exchange插件
 * 先启动消费者
 * @author we
 * @date 2021-07-13 16:16
 **/
public class DelayPluginProducer {
    public static void main(String[] args) throws Exception{
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUri("amqp://guest:guest@127.0.0.1:5672");

        // 建立连接
        Connection conn = factory.newConnection();
        // 创建消息通道
        Channel channel = conn.createChannel();

        // 延时投递，比如延时1分钟
        Date now = new Date();
        Calendar calendar = Calendar.getInstance();
        // 1分钟后投递
        calendar.add(Calendar.MINUTE, +1);
        Date delayTime = calendar.getTime();

        // 定时投递，把这个值替换delayTime即可
        // Date exactDealyTime = new Date("2019/01/14,22:30:00");

        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        String msg = "发送时间：" + sf.format(now) + "，需要投递时间：" + sf.format(delayTime);

        // 延迟的间隔时间，目标时刻减去当前时刻
        Map<String, Object> headers = new HashMap<>();
        headers.put("x-delay", delayTime.getTime() - now.getTime());

        AMQP.BasicProperties.Builder props = new AMQP.BasicProperties.Builder()
                .headers(headers);
        channel.basicPublish("DELAY_EXCHANGE", "DELAY_KEY", props.build(),
                msg.getBytes());

        channel.close();
        conn.close();
    }
}
