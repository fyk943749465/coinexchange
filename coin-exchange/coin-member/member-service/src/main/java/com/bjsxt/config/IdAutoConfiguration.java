package com.bjsxt.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Configuration
@EnableConfigurationProperties(IdProperties.class)
public class IdAutoConfiguration {

    private static IdProperties idProperties;
    // Spring 提供的一个发送http请求的工具
    private static RestTemplate restTemplate = new RestTemplate();

    public IdAutoConfiguration(IdProperties idProperties) {
        IdAutoConfiguration.idProperties = idProperties;
    }

    /**
     *
     * @param realName 用户的真实姓名
     * @param carNum 用户的身份证号码
     * @return 验证的结果
     */
    public static boolean check(String realName, String carNum) {
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        paramMap.put("cardNo", Collections.singletonList(carNum));
        paramMap.put("realName", Collections.singletonList(realName));

        HttpHeaders headers = new HttpHeaders();
        headers.put("Authorization", Collections.singletonList("APPCODE " + idProperties.getAppCode()));
        headers.put("Content-Type", Collections.singletonList("application/x-www-form-urlencoded; charset=UTF-8"));

        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<MultiValueMap<String, Object>>(paramMap, headers);
        ResponseEntity<String> response = restTemplate.exchange(idProperties.getUrl(), HttpMethod.POST, httpEntity, String.class);
        System.out.println(response.getStatusCode());
        System.out.println(response.getBody());
        if (response.getStatusCode() == HttpStatus.OK) {
            String body = response.getBody();
            JSONObject jsonObject = JSON.parseObject(body);
            int staus = jsonObject.getInteger("error_code");
            JSONObject result = jsonObject.getJSONObject("result");
            Boolean isok = result.getBoolean("isok");

            if (staus == 0 && isok) {
                return true;
            }
        }
        return false;
    }
}
