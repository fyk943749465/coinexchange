package com.bjsxt.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "identify")
@Data
public class IdProperties {

    private String url; // 身份认证的url地址// https://zid.market.alicloudapi.com/idcheck/Post

    private String appKey;

    private String appSecret;

    private String appCode;

}
