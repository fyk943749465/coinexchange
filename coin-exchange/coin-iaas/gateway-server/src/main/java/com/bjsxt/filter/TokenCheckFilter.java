package com.bjsxt.filter;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Set;

/**
 * The filter in the gateway can intercept all requests.
 *
 */
@Component
public class TokenCheckFilter implements GlobalFilter, Ordered {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Value("${no.token.access.urls:/admin/login,/user/gt/register,/user/login,/user/users/register}") //网关放行注册地址
    private Set<String> noTokenAccessUrls;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        if (allowNoTokenAccess(exchange)) {
            return chain.filter(exchange);
        }

        String token = getToken(exchange);

        if (StringUtils.isEmpty(token)) {
            return buildUNAuthorizationResult(exchange);
        }

        Boolean hasKey = redisTemplate.hasKey(token);
        if (hasKey != null && hasKey) {
            return chain.filter(exchange);
        }
        return buildUNAuthorizationResult(exchange);
    }

    private Mono<Void> buildUNAuthorizationResult(ServerWebExchange exchange) {

        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().set("Content-Type", "application/json;charset=UTF-8");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("error", "unauthorized");
        jsonObject.put("error_description", "invalid_token");
        DataBuffer dataBuffer = response.bufferFactory().wrap(jsonObject.toJSONString().getBytes(StandardCharsets.UTF_8));
        return response.writeWith(Flux.just(dataBuffer));
    }

    private String getToken(ServerWebExchange exchange) {

        ServerHttpRequest request = exchange.getRequest();
        HttpHeaders headers = request.getHeaders();
        String authorization = headers.getFirst(HttpHeaders.AUTHORIZATION);
        if (Objects.isNull(authorization) || authorization.trim().isEmpty()
        || authorization.replace("bearer ", "").isEmpty()){
            return null;
        }
        return authorization.replace("bearer ", "");
    }

    private boolean allowNoTokenAccess(ServerWebExchange exchange) {

        String path = exchange.getRequest().getURI().getPath();
        if (noTokenAccessUrls.contains(path)) {
            return true;
        }
        return false;
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
