package pers.ervin.kafka;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

/**
 * Kafka的生产者程序
 * 会将消息创建出来，并发送到Kafka集群中
 *
 * 1. 创建用于连接Kafka的Properties配置
 * Properties props = new Properties();
 * props.put("bootstrap.servers", "192.168.88.100:9092");
 * props.put("acks", "all");
 * props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
 * props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
 *
 * 2. 创建一个生产者对象KafkaProducer
 * 3. 调用send发送1-100消息到指定Topic test，并获取返回值Future，该对象封装了返回值
 * 4. 再调用一个Future.get()方法等待响应
 * 5. 关闭生产者
 */
public class KafkaProducerTest2 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // 1. 创建用于连接Kafka的Properties配置
        Properties props = new Properties();
        props.put("bootstrap.servers", "node1.xx.xx:9092");
        props.put("acks", "all");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        // 2. 创建一个生产者对象KafkaProducer
        KafkaProducer<String, String> kafkaProducer = new KafkaProducer<>(props);

        int MAX = 10000000;

        // 3. 发送1-100的消息到指定的topic中
        for(int i = 1000000; i < MAX; ++i) {
            // 一、使用同步等待的方式发送消息
            // // 构建一条消息，直接new ProducerRecord
            // ProducerRecord<String, String> producerRecord = new ProducerRecord<>("test", null, i + "");
            // Future<RecordMetadata> future = kafkaProducer.send(producerRecord);
            // // 调用Future的get方法等待响应
            // future.get();
            // System.out.println("第" + i + "条消息写入成功！");

            // 二、使用异步回调的方式发送消息
            ProducerRecord<String, String> producerRecord = new ProducerRecord<>("test1", null, i + "");
            kafkaProducer.send(producerRecord, new Callback() {
                @Override
                public void onCompletion(RecordMetadata metadata, Exception exception) {
                    // 1. 判断发送消息是否成功
                    if(exception == null) {
                        // 发送成功
                        // 主题
                        String topic = metadata.topic();
                        // 分区id
                        int partition = metadata.partition();
                        // 偏移量
                        long offset = metadata.offset();
                        System.out.println("topic:" + topic + " 分区id：" + partition + " 偏移量：" + offset);
                    }
                    else {
                        // 发送出现错误
                        System.out.println("生产消息出现异常！");
                        // 打印异常消息
                        System.out.println(exception.getMessage());
                        // 打印调用栈
                        System.out.println(exception.getStackTrace());
                    }
                }
            });

            Thread.sleep(1000);
        }

        // 4.关闭生产者
        kafkaProducer.close();
    }
}
