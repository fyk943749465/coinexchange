package com.bjsxt.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bjsxt.domain.AdminAddress;
import com.baomidou.mybatisplus.extension.service.IService;
public interface AdminAddressService extends IService<AdminAddress>{


    Page<AdminAddress> findByPage(Page<AdminAddress> page, Long coinId);

    /**
     * 重写save方法，需要找到币种类的类型
     * @param entity
     * @return
     */
    @Override
    boolean save(AdminAddress entity);
}
