package com.bjsxt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.tio.core.Tio;
import org.tio.websocket.common.WsResponse;
import org.tio.websocket.starter.EnableTioWebSocketServer;
import org.tio.websocket.starter.TioWebSocketServerBootstrap;

import java.util.Date;

@SpringBootApplication
@EnableDiscoveryClient
@EnableTioWebSocketServer
//@EnableScheduling
public class ChanServiceApplication {

    @Autowired
    private TioWebSocketServerBootstrap bootstrap;

    public static void main(String[] args) {
        SpringApplication.run(ChanServiceApplication.class);
    }

    /**
     * 每5秒推送数据到客户端
     */
//    @Scheduled(fixedRate = 5000)
//    public void pushData() {
//        long time = new Date().getTime();
//        Tio.sendToGroup(bootstrap.getServerTioConfig(), "test", WsResponse.fromText("now " + time, "utf-8"));
//    }
}
