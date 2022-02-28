package com.bjsxt.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bjsxt.domain.CoinType;
import com.baomidou.mybatisplus.extension.service.IService;
public interface CoinTypeService extends IService<CoinType>{

    /**
     * 条件分页查询货币类型
     * @param page
     * @param code
     * @return 分页货币类型
     */
    Page<CoinType> findByPage(Page<CoinType> page, String code);
}
