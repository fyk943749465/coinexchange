package com.bjsxt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bjsxt.domain.CoinType;
import com.bjsxt.mapper.CoinTypeMapper;
import com.bjsxt.service.CoinTypeService;
import org.springframework.util.StringUtils;

@Service
public class CoinTypeServiceImpl extends ServiceImpl<CoinTypeMapper, CoinType> implements CoinTypeService{

    @Override
    public Page<CoinType> findByPage(Page<CoinType> page, String code) {
        page.addOrder(OrderItem.desc("last_update_time"));
        return page(page, new LambdaQueryWrapper<CoinType>().like(!StringUtils.isEmpty(code), CoinType::getCode, code));
    }
}
