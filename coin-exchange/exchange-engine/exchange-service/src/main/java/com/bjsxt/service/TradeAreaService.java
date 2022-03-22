package com.bjsxt.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bjsxt.domain.TradeArea;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bjsxt.vo.TradeAreaMarketVo;

import java.util.List;

public interface TradeAreaService extends IService<TradeArea>{


    Page<TradeArea> findByPage(Page<TradeArea> page, String name, Byte status);

    List<TradeArea> findAll(Byte status);

    List<TradeAreaMarketVo> findTradeAreaMarket();

    List<TradeAreaMarketVo> getUserFavoriteMarkets(Long userId);
}
