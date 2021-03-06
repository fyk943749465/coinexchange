package com.bjsxt.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bjsxt.domain.CashWithdrawAuditRecord;
import com.bjsxt.domain.CashWithdrawals;
import com.baomidou.mybatisplus.extension.service.IService;
public interface CashWithdrawalsService extends IService<CashWithdrawals>{


    Page<CashWithdrawals> findByPage(Page<CashWithdrawals> page, Long userId, String userName, String mobile, Byte status, String numMin, String numMax, String startTime, String endTime);

    boolean updateWithdrawalsStatus(Long userId, CashWithdrawAuditRecord cashWithdrawAuditRecord);

    Page<CashWithdrawals> findCashWithdrawals(Page<CashWithdrawals> page, Long userId, Byte status);
}
