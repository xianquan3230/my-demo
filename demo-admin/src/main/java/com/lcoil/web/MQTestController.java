package com.lcoil.web;

import com.lcoil.common.core.domain.AjaxResult;
import com.lcoil.rocketmq.Producer;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Classname MQTestController
 * @Description TODO
 * @Date 2022/5/7 8:25 AM
 * @Created by l-coil
 */
@RestController
@RequestMapping("/testRocketMq")
public class MQTestController {
    private final String topic = "TestTopic";
    @Resource
    private Producer producer;

    @RequestMapping("/sendMessage")
    public AjaxResult sendMessage(String message) {
        producer.sendMessage(topic, message);
        return AjaxResult.success("消息发送成功");
    }

    //这个发送事务消息的例子中有很多问题，需要注意下。
    @RequestMapping("/sendTransactionMessage")
    public String sendTransactionMessage(String message) throws InterruptedException {
        producer.sendMessageInTransaction(topic, message);
        return "消息发送完成";
    }
}
