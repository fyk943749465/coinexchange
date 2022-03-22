package com.bjsxt.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bjsxt.domain.Market;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bjsxt.dto.MarketDto;

import java.util.List;

public interface MarketService extends IService<Market>{


    Page<Market> findByPage(Page<Market> page, Long tradeAreaId, Byte status);

    List<Market> getMarkersByTradeAreaId(Long id);

    Market getMarkerBySymbol(String symbol);

    MarketDto findByCoinId(Long buyCoinId, Long sellCoinId);
}
