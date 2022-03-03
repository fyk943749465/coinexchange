package com.bjsxt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bjsxt.mapper.CoinMapper;
import com.bjsxt.domain.Coin;
import com.bjsxt.service.CoinService;
import org.springframework.util.StringUtils;

@Service
public class CoinServiceImpl extends ServiceImpl<CoinMapper, Coin> implements CoinService{

    @Override
    public Page<Coin> findByPage(String name, String type, Byte status, String title, String walletType, Page<Coin> page) {

        return page(page, new LambdaQueryWrapper<Coin>()
                .like(!StringUtils.isEmpty(name), Coin::getName, name) // 数字货币名称的查询
                .like(!StringUtils.isEmpty(title), Coin::getTitle, title) // 数字货币标题的查询
                .eq(status != null, Coin::getStatus, status)  // 数字货币状态的查询
                .eq(!StringUtils.isEmpty(type), Coin::getType, type)  // 数字货币类型的查询
                .eq(!StringUtils.isEmpty(walletType), Coin::getWallet, walletType) //数字货币钱包类型的查询
        );
    }

    @Override
    public List<Coin> getCoinByStatus(Byte status) {
        return list(new LambdaQueryWrapper<Coin>().eq(Coin::getStatus, status));
    }

    @Override
    public Coin getCoinByCoinName(String coinName) {
        return getOne(new LambdaQueryWrapper<Coin>().eq(Coin::getName,coinName));
    }
}
