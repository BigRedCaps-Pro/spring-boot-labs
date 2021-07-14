package com.we.javaapi.confirm;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.we.javaapi.util.ResourceUtil;

/**
 * 批量确认：每发送一批消息后，调用channel.waitForConfirms()方法，同步等待服务器端confirm。
 * @author we
 * @date 2021-07-14 16:10
 **/
public class BatchConfirmProducer {

    public static final String QUEUE_NAME = "ORIGIN_QUEUE";

    public static void main(String[] args) throws Exception{
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUri(ResourceUtil.getKey("rabbitmq.uri"));

        Connection conn = factory.newConnection();
        Channel channel = conn.createChannel();

        String msg = "Hello world,RabbitMQ,Batch confirm";
        // 声明队列（默认交换机AMQP default,Direct）
        // String queue,boolean durable,boolean exclusive,boolean autoDelete,Map<String,Object> map
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);

        try {
            channel.confirmSelect();
            for (int i = 0; i < 5000; i++) {
                // 发送消息
                //String exchange,String routingKey,BasicProperties props,byte[] body
                channel.basicPublish("",QUEUE_NAME,null,(msg+"-"+i).getBytes());
            }
            // 批量确认结果，ACK如果是Multiple=True，代表ACK里面的Delivery-Tag之前的消息都被确认了
            // 比如5条消息可能只收到1个ACK，也可能收到2个（抓包才看得到）
            // 直到所有信息都发布，只要有一个未被Broker确认就会IOException
            channel.waitForConfirmsOrDie();
            System.out.println("消息发送完毕，批量确认成功");
        }catch (Exception e){
            // 发生异常，可能需要对所有消息进行转发
            e.printStackTrace();
        }
    }

}
