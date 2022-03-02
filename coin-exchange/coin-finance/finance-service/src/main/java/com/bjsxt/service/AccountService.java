package com.bjsxt.service;

import com.bjsxt.domain.Account;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;

public interface AccountService extends IService<Account>{


    /**
     * 用户资金的划转
     * @param adminId
     * @param userId
     * @param coinId
     * @param num
     * @param fee
     * @param remark
     * @param businessType
     * @param direction
     * @return
     */
    Boolean transferAccountAmount(Long adminId, Long userId, Long coinId,Long orderNum , BigDecimal num, BigDecimal fee,String remark,String businessType,Byte direction);

}
