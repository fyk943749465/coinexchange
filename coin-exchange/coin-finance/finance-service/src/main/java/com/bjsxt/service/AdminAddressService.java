package com.bjsxt.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bjsxt.domain.AdminAddress;
import com.baomidou.mybatisplus.extension.service.IService;
public interface AdminAddressService extends IService<AdminAddress>{


    Page<AdminAddress> findByPage(Page<AdminAddress> page, Long coinId);
}
