package com.bjsxt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bjsxt.domain.TurnoverOrder;
import com.bjsxt.mapper.TurnoverOrderMapper;
import com.bjsxt.service.TurnoverOrderService;
@Service
public class TurnoverOrderServiceImpl extends ServiceImpl<TurnoverOrderMapper, TurnoverOrder> implements TurnoverOrderService{

    @Override
    public Page<TurnoverOrder> findByPage(Page<TurnoverOrder> page, Long userId, String symbol, Integer type) {
        return null;
    }

    /**
     * 获取买入的订单的成功的记录
     *
     * @param orderId
     * @return
     */
    @Override
    public List<TurnoverOrder> getBuyTurnoverOrder(Long orderId, Long userId) {
        return list(new LambdaQueryWrapper<TurnoverOrder>().eq(TurnoverOrder::getOrderId, orderId)
                .eq(TurnoverOrder::getBuyUserId, userId)
        );
    }


    /**
     * 获取卖出订单的成交记录
     *
     * @param orderId
     * @return
     */
    @Override
    public List<TurnoverOrder> getSellTurnoverOrder(Long orderId,Long userId) {
        return list(new LambdaQueryWrapper<TurnoverOrder>().eq(TurnoverOrder::getOrderId, orderId)
                .eq(TurnoverOrder::getSellUserId, userId)
        );

    }

    /**
     * 根据交易市场查询我们的成交记录
     *
     * @param symbol
     * @return
     */
    @Override
    public List<TurnoverOrder> findBySymbol(String symbol) {
        List<TurnoverOrder> turnoverOrders = list(
                new LambdaQueryWrapper<TurnoverOrder>()
                        .eq(TurnoverOrder::getSymbol, symbol)
                        .orderByDesc(TurnoverOrder::getCreated)
                        .eq(TurnoverOrder::getStatus,1)
                        .last("limit 60")
        );
        return turnoverOrders;
    }
}
