package com.lcoil.rocketmq;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * @Classname Consumer
 * @Description TODO
 * @Date 2022/5/7 8:21 AM
 * @Created by l-coil
 */
@Component
@RocketMQMessageListener(consumerGroup = "springBootGroup", topic = "TestTopic")
public class Consumer implements RocketMQListener {

    @Override
    public void onMessage(Object message) {
        System.out.println("Received message : "+ message);
    }
}
