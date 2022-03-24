package com.bjsxt.match.impl;

import com.bjsxt.enums.MatchStrategy;
import com.bjsxt.match.MatchService;
import com.bjsxt.match.MatchServiceFactory;
import com.bjsxt.model.Order;
import com.bjsxt.model.OrderBooks;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class LimitPriceMatchServiceImpl implements MatchService, InitializingBean {
    @Override
    public void match(OrderBooks orderBooks, Order order) {
        log.info("开始撮合");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        MatchServiceFactory.addMatchService(MatchStrategy.LIMIT_PRICE, this); // 交易策略
    }
}
