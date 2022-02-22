import com.ldg.cloud.ProductApplication;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.MessageSelector;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ProductApplication.class)
public class RecMqTest {

    @Test
    public  void testSend() throws MQClientException, MQBrokerException, RemotingException, InterruptedException {
        //生成者组
        DefaultMQProducer producer = new DefaultMQProducer("producer01");
        producer.setNamesrvAddr("47.106.132.130:9876");
        producer.start();
        //mqTopic：主题，相当于交换机
        //tag:分组，相当于队列，分配到相应的队列
        Message message = new Message("mqTopic", "mqTag", "data".getBytes());
        //设置消息属性，用于sql过滤
        message.putUserProperty("","");
        SendResult send = producer.send(message);
        System.out.println(send);
        producer.shutdown();
        System.out.println("---生成者结束---");
    }
    @Test
    public void testRecicve() throws MQClientException, InterruptedException {

        //消费者组
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("consumer01");

        consumer.setNamesrvAddr("47.106.132.130:9876");
        //过滤器，要消费那些消息
        //mqTopic：主题，相当于交换机
        //tag:分组，相当于队列，分配到相应的队列

        //消息过滤
        MessageSelector messageSelector = MessageSelector.bySql("age>18 and age< 30");
        //consumer.subscribe("mqTopic", messageSelector);
        consumer.subscribe("mqTopic", "mqTag");

        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
                byte[] body = list.get(0).getBody();
                String s = new String(body);
                System.out.println("-----------------");
                System.out.println(s);
                System.out.println("接收到新消息"+list);
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        consumer.start();
        System.out.println("----------消费者结束-----------");
        Thread.sleep(10000);
    }
}
