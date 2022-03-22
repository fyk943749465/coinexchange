package com.bjsxt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bjsxt.mapper.EntrustOrderMapper;
import com.bjsxt.domain.EntrustOrder;
import com.bjsxt.service.EntrustOrderService;
import org.springframework.util.MimeTypeUtils;
import org.springframework.util.StringUtils;

@Service
public class EntrustOrderServiceImpl extends ServiceImpl<EntrustOrderMapper, EntrustOrder> implements EntrustOrderService{
    /**
     * 分页查询委托单
     *
     * @param page   分页参数
     * @param userId 用户的id
     * @param symbol 交易对
     * @param type   交易类型
     * @return
     */
    @Override
    public Page<EntrustOrder> findByPage(Page<EntrustOrder> page, Long userId, String symbol, Integer type) {
        return page(page,
                new LambdaQueryWrapper<EntrustOrder>()
                        .eq(EntrustOrder::getUserId, userId)
                        .eq(!StringUtils.isEmpty(symbol), EntrustOrder::getSymbol, symbol)
                        .eq(type != null && type != 0, EntrustOrder::getType, type)
                        .orderByDesc(EntrustOrder::getCreated)

        );
    }

    @Override
    public void cancleEntrustOrder(Long orderId) {
//        // 取消委托单
//        // 1 将该委托单从撮合引擎里面的委托单账本里面移除
//        EntrustOrder entrustOrder = new EntrustOrder();
//        entrustOrder.setStatus((byte) 2);
//        entrustOrder.setId(orderId);
//        Message<EntrustOrder> message = MessageBuilder.withPayload(entrustOrder).setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON).build();
//        source.outputMessage().send(message);

    }
}
