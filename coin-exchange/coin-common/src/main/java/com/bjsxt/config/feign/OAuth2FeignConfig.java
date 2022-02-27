package com.bjsxt.config.feign;

import com.bjsxt.constant.Constants;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 实现feign提供的interceptor
 * 解决远程调用过程中，token的传递问题
 */
@Slf4j
public class OAuth2FeignConfig implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        // 1. 我们可以从request的上下文里获取token
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        String header = null;
        if (requestAttributes == null) {
            log.info("没有请求的上下文，故无法进行token的传递");
            header = "bearer "+ Constants.INSIDE_TOKEN ;
        } else {
            // 如果不为空，我们获取
            HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
            header = request.getHeader(HttpHeaders.AUTHORIZATION); //获取我们请求上下文头里面的AUTHORIZATION
        }
        if (!StringUtils.isEmpty(header)) {
            requestTemplate.header(HttpHeaders.AUTHORIZATION, header); // 远程请求是，将原来请求头原封不动传递
            log.info("本次token传递成功，token的值为{}", header);
        }
    }
}
