package producer;

import com.ldg.cloud.MainApplication;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Properties;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MainApplication.class)
public class CustomProducerCallbackPartitions {

    @Test
    public void ack() throws InterruptedException {
        Properties properties = new Properties();

        MyPartitioner myPartitioner = new MyPartitioner();

        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"47.106.132.130:9093");
        // 指定对应的key和value的序列化类型 key.serializer
//        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringSerializer");
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,StringSerializer.class.getName());

        properties.put(ProducerConfig.PARTITIONER_CLASS_CONFIG,"producer.MyPartitioner");

        // 1 创建kafka生产者对象
        // "" hello
        KafkaProducer<String, String> kafkaProducer = new KafkaProducer<>(properties);

        // 2 发送数据
        for (int i = 0; i < 5; i++) {
            kafkaProducer.send(new ProducerRecord<>("partopic", "","ldg" + i), new Callback() {
                @Override
                public void onCompletion(RecordMetadata metadata, Exception exception) {

                    if (exception == null){
                        System.out.println("主题： "+metadata.topic() + " 分区： "+ metadata.partition());
                    }
                }
            });

           // Thread.sleep(2);
        }

        // 3 关闭资源
        kafkaProducer.close();

    }
}
