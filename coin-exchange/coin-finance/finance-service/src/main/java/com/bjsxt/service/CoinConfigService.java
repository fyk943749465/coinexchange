package com.bjsxt.service;

import com.bjsxt.domain.CoinConfig;
import com.baomidou.mybatisplus.extension.service.IService;
public interface CoinConfigService extends IService<CoinConfig>{


    /**
     * 通过币种的id查询币种的配置信息
     * @param coinId
     * @return
     */
    CoinConfig findByCoinId(Long coinId);

    boolean saveOrUpdate(CoinConfig entity);
}
