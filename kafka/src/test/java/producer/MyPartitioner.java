package producer;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;

import java.util.Map;

public class MyPartitioner implements Partitioner {
    @Override
    public int partition(String s, Object key, byte[] bytes, Object value, byte[] bytes1, Cluster cluster) {
       int partition;
       String msgValue=value.toString();
       msgValue=msgValue.substring(3);
       int num=Integer.parseInt(msgValue);
       if(num%2==0){
           partition=1;
       }else{
           partition=0;
       }
        return partition;
    }

    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> map) {

    }
}
