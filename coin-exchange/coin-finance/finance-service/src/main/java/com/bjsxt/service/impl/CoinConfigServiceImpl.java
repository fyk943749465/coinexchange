package com.bjsxt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bjsxt.mapper.CoinConfigMapper;
import com.bjsxt.domain.CoinConfig;
import com.bjsxt.service.CoinConfigService;
@Service
public class CoinConfigServiceImpl extends ServiceImpl<CoinConfigMapper, CoinConfig> implements CoinConfigService{

    @Override
    public CoinConfig findByCoinId(Long coinId) {
        // coinConfig的id和Coin中的id，是同一个id
        return getOne(new LambdaQueryWrapper<CoinConfig>().eq(CoinConfig::getId, coinId));
    }
}
