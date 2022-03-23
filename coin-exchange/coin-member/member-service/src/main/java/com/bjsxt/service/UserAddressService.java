package com.bjsxt.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bjsxt.domain.UserAddress;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface UserAddressService extends IService<UserAddress>{


    Page<UserAddress> findByPage(Page<UserAddress> page, Long userId);

    UserAddress getUserAddressByUserIdAndCoinId(String userId, Long coinId);

    List<UserAddress> getUserAddressByUserId(Long valueOf);
}
