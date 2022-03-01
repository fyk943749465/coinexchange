package com.bjsxt.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bjsxt.domain.Coin;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface CoinService extends IService<Coin>{


    /**
     * 数字货币的条件分页查询
     * @param name 数字货币的名称
     * @param type 数字货币类型的名称
     * @param status 数字货币的状态
     * @param title  数字货币的标题
     * @param walletType 数字货币的钱包类型名称
     * @param page 数字货币的分页数据
     * @return
     */
    Page<Coin> findByPage(String name, String type, Byte status, String title, String walletType, Page<Coin> page);

    /**
     * 使用币种的状态查询所有的币
     * @param status
     * @return
     */
    List<Coin> getCoinByStatus(Byte status);
}
