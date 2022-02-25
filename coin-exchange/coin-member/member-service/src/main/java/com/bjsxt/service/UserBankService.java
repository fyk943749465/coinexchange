package com.bjsxt.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bjsxt.domain.UserBank;
import com.baomidou.mybatisplus.extension.service.IService;
public interface UserBankService extends IService<UserBank>{


    Page<UserBank> findByPage(Page<UserBank> page, Long usrId);

    /**
     * 通过用户的id查询用户的银行卡
     * @param valueOf
     * @return
     */
    UserBank getCurrentUserBank(Long valueOf);
}
