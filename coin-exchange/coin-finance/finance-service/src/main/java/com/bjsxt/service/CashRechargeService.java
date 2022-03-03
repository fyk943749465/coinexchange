package com.bjsxt.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bjsxt.domain.CashRecharge;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bjsxt.domain.CashRechargeAuditRecord;

public interface CashRechargeService extends IService<CashRecharge>{


    Page<CashRecharge> findByPage(Page<CashRecharge> page, Long coinId, Long userId, String userName, String mobile, Byte status, String numMin, String numMax, String startTime, String endTime);

    boolean cashRechargeAudit(Long userId, CashRechargeAuditRecord cashRechargeAuditRecord);

    /**
     * 查询当前用户的充值的数据
     * @param page
     * 分页对象
     * @param userId
     * 用户的Id
     * @param status
     * 订单的状态
     * @return
     */
    Page<CashRecharge> findUserCashRecharge(Page<CashRecharge> page, Long userId, Byte status);
}
