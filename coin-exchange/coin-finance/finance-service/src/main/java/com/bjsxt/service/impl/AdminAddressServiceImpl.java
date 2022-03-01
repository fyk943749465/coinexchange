package com.bjsxt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bjsxt.domain.Coin;
import com.bjsxt.service.CoinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bjsxt.mapper.AdminAddressMapper;
import com.bjsxt.domain.AdminAddress;
import com.bjsxt.service.AdminAddressService;
import org.springframework.util.StringUtils;

@Service
public class AdminAddressServiceImpl extends ServiceImpl<AdminAddressMapper, AdminAddress> implements AdminAddressService{

    @Autowired
    private CoinService coinService;
    /**
     * 分页条件查询钱包提币的归集地址
     * @param page
     * @param coinId
     * @return
     */
    @Override
    public Page<AdminAddress> findByPage(Page<AdminAddress> page, Long coinId) {
        return page(page, new LambdaQueryWrapper<AdminAddress>().eq(AdminAddress::getCoinId, coinId));
    }

    @Override
    public boolean save(AdminAddress adminAddress) {

        Long coinId = adminAddress.getCoinId();
        Coin coin = coinService.getById(coinId);
        if (coin == null) {
            throw new IllegalArgumentException("不合法的参数");
        }
        adminAddress.setCoinType(coin.getType());
        return super.save(adminAddress);
    }
}
