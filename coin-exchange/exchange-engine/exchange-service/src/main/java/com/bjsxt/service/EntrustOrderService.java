package com.bjsxt.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bjsxt.domain.EntrustOrder;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bjsxt.vo.OrderParamVo;
import com.bjsxt.vo.TradeEntrustOrderVo;

public interface EntrustOrderService extends IService<EntrustOrder>{


    Page<EntrustOrder> findByPage(Page<EntrustOrder> page, Long userId, String symbol, Integer type);

    void cancleEntrustOrder(Long orderId);

    Page<TradeEntrustOrderVo> getHistoryEntrustOrder(Page<EntrustOrder> page, String symbol, Long userId);

    Page<TradeEntrustOrderVo> getEntrustOrder(Page<EntrustOrder> page, String symbol, Long userId);

    Boolean createEntrustOrder(Long userId, OrderParamVo orderParam);
}
