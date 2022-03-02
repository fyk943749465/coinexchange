package com.bjsxt.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bjsxt.domain.CashRecharge;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bjsxt.domain.CashRechargeAuditRecord;

public interface CashRechargeService extends IService<CashRecharge>{


    Page<CashRecharge> findByPage(Page<CashRecharge> page, Long coinId, Long userId, String userName, String mobile, Byte status, String numMin, String numMax, String startTime, String endTime);

    boolean cashRechargeAudit(Long userId, CashRechargeAuditRecord cashRechargeAuditRecord);
}
