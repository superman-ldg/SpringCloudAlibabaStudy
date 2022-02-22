package com.ldg.cloud.rocketmq;

import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.springframework.stereotype.Component;

@Component
public class MySendCallBack implements SendCallback {
    @Override
    public void onSuccess(SendResult sendResult) {

    }

    @Override
    public void onException(Throwable throwable) {

    }
}
