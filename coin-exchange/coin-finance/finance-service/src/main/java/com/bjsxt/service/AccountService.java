package com.bjsxt.service;

import com.bjsxt.domain.Account;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bjsxt.vo.UserTotalAccountVo;

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
    /**
     * 给用户扣减钱
     * @param adminId
     *   操作的人
     * @param userId
     * 用户的id
     * @param coinId
     * 币种的id
     * @param orderNum
     * 订单的编号
     * @param num
     * 扣减的余额
     * @param fee
     * 费用
     * @param remark
     * 备注
     * @param businessType
     * 业务类型
     * @param direction
     * 方向
     * @return
     */
    Boolean decreaseAccountAmount(Long adminId, Long userId, Long coinId, Long orderNum ,BigDecimal num, BigDecimal fee,String remark, String businessType, byte direction);
    /**
     * 查询某个用户的货币资产
     * @param userId
     *  用户的id
     * @param coinName
     * 货币的名称
     * @return
     */
    Account findByUserAndCoin(Long userId, String coinName);

    UserTotalAccountVo getUserTotalAccount(Long userId);
}
