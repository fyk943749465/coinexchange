package com.bjsxt.config.rocket;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

/**
 * 发送消息
 */
public interface Source {

    @Output("order_out")
    MessageChannel outputMessage() ;
}
