
import com.ldg.cloud.RocketMqApplication;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.*;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RocketMqApplication.class)
public class RecMqTest {

    @Test
    public  void testSend() throws MQClientException, MQBrokerException, RemotingException, InterruptedException {
        DefaultMQProducer producer = new DefaultMQProducer("producer01");
        producer.setNamesrvAddr("47.106.132.130:9876");
        producer.start();
       // producer.setRetryTimesWhenSendAsyncFailed();重试
        Message message = new Message("mqTopic", "mqTag", "data".getBytes());
        //message.setDelayTimeLevel();
        SendResult send = producer.send(message);
        //队列现选择器
        producer.send(message, new MessageQueueSelector() {
            @Override
            public MessageQueue select(List<MessageQueue> list, Message message, Object o) {
                return null;
            }
        },null);
        System.out.println(send);
        producer.shutdown();
        System.out.println("---生成者结束---");

    }
    @Test
    public void testRecicve() throws MQClientException, InterruptedException {

        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("consumer01");

        consumer.setNamesrvAddr("47.106.132.130:9876");
        consumer.subscribe("mqTopic", "mqTag");

        //串行消费
        consumer.registerMessageListener(new MessageListenerOrderly() {
            @Override
            public ConsumeOrderlyStatus consumeMessage(List<MessageExt> list, ConsumeOrderlyContext consumeOrderlyContext) {
                return null;
            }
        });//

        //并行消费
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
