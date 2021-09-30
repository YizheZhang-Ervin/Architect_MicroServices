package pers.ervin.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.KafkaException;
import org.apache.kafka.common.TopicPartition;

import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Future;

public class TransactionProgram {
    public static void main(String[] args) {
        // 1. 调用之前实现的方法，创建消费者、生产者对象
        KafkaConsumer<String, String> consumer = createConsumer();
        KafkaProducer<String, String> producer = createProducer();

        // 2. 生产者调用initTransactions初始化事务
        producer.initTransactions();

        // 3. 编写一个while死循环，在while循环中不断拉取数据，进行处理后，再写入到指定的topic
        while(true) {
            try {
                // (1)	生产者开启事务
                producer.beginTransaction();

                // 这个Map保存了topic对应的partition的偏移量
                Map<TopicPartition, OffsetAndMetadata> offsetMap = new HashMap<>();

                // 从topic中拉取一批的数据
                // (2)	消费者拉取消息
                ConsumerRecords<String, String> concumserRecordArray = consumer.poll(Duration.ofSeconds(5));
                // (3)	遍历拉取到的消息，并进行预处理
                for (ConsumerRecord<String, String> cr : concumserRecordArray) {
                    // 将1转换为男，0转换为女
                    String msg = cr.value();
                    String[] fieldArray = msg.split(",");

                    // 将消息的偏移量保存
                    // 消费的是ods_user中的数据
                    String topic = cr.topic();
                    int partition = cr.partition();
                    long offset = cr.offset();

                    // offset + 1：offset是当前消费的记录（消息）对应在partition中的offset，而我们希望下一次能继续从下一个消息消息
                    // 必须要+1，从能消费下一条消息
                    offsetMap.put(new TopicPartition(topic, partition), new OffsetAndMetadata(offset + 1));

                    // 将字段进行替换
                    if(fieldArray != null && fieldArray.length > 2) {
                        String sexField = fieldArray[1];
                        if(sexField.equals("1")) {
                            fieldArray[1] = "男";
                        }
                        else if(sexField.equals("0")){
                            fieldArray[1] = "女";
                        }
                    }

                    // 重新拼接字段
                    msg = fieldArray[0] + "," + fieldArray[1] + "," + fieldArray[2];

                    // (4)	生产消息到dwd_user topic中
                    ProducerRecord<String, String> dwdMsg = new ProducerRecord<>("dwd_user", msg);
                    // 发送消息
                    Future<RecordMetadata> future = producer.send(dwdMsg);
                    try {
                        future.get();
                    } catch (Exception e) {
                        e.printStackTrace();
                        producer.abortTransaction();
                    }
                }
                producer.sendOffsetsToTransaction(offsetMap, "ods_user");

                int i = 1 / 0;

                // (6)	提交事务
                producer.commitTransaction();
            }catch (Exception e) {
                e.printStackTrace();
                // (7)	捕获异常，如果出现异常，则取消事务
                producer.abortTransaction();
            }
        }
    }

    // 一、创建一个消费者来消费ods_user中的数据
    private static KafkaConsumer<String, String> createConsumer() {
        // 1. 配置消费者的属性（添加对事务的支持）
        Properties props = new Properties();
        props.setProperty("bootstrap.servers", "node1.xx.xx:9092");
        props.setProperty("group.id", "ods_user");
        // 配置事务的隔离级别
        props.put("isolation.level","read_committed");
        // 关闭自动提交，一会我们需要手动来提交offset，通过事务来维护offset
        props.setProperty("enable.auto.commit", "false");
        // 反序列化器
        props.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.setProperty("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        // 2. 构建消费者对象
        KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(props);

        // 3. 订阅一个topic
        kafkaConsumer.subscribe(Arrays.asList("ods_user"));

        return kafkaConsumer;

    }

    // 二、编写createProducer方法，用来创建一个带有事务配置的生产者
    private static KafkaProducer<String, String> createProducer() {
        // 1. 配置生产者带有事务配置的属性
        Properties props = new Properties();
        props.put("bootstrap.servers", "node1.xx.xx:9092");
        props.put("acks", "all");
        // 开启事务必须要配置事务的ID
        props.put("transactional.id", "dwd_user");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        // 2. 构建生产者
        KafkaProducer<String, String> kafkaProducer = new KafkaProducer<>(props);

        return kafkaProducer;
    }
}
