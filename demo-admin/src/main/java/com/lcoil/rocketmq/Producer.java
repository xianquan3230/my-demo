package com.lcoil.rocketmq;

import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

/**
 * @Classname Producer
 * @Description TODO
 * @Date 2022/5/7 8:20 AM
 * @Created by l-coil
 */
@Component
public class Producer {
    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    // 发送消息的实例
    public void sendMessage(String topic, String msg) {
        rocketMQTemplate.convertAndSend(topic, msg);
    }

    // 发送事务消息的实例
    public void sendMessageInTransaction(String topic, String msg) throws InterruptedException {
        String[] tags = {"TagA", "TagB", "TagC", "TagD", "TagE"};
        for (int i = 0; i < 10; i++) {
            Message<String> message = MessageBuilder.withPayload(msg).build();
            String destination = topic + ":" + tags[i % tags.length];

            TransactionSendResult sendResult =
                    rocketMQTemplate.sendMessageInTransaction(destination, message, destination);

            System.out.printf("%s%n", sendResult);
            Thread.sleep(10);
        }
    }
}
