package com.bjsxt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bjsxt.mapper.AdminAddressMapper;
import com.bjsxt.domain.AdminAddress;
import com.bjsxt.service.AdminAddressService;
@Service
public class AdminAddressServiceImpl extends ServiceImpl<AdminAddressMapper, AdminAddress> implements AdminAddressService{

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
}
