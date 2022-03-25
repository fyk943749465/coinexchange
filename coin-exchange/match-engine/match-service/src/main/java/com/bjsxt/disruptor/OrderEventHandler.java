package com.bjsxt.disruptor;

import com.bjsxt.enums.MatchStrategy;
import com.bjsxt.match.MatchServiceFactory;
import com.bjsxt.model.Order;
import com.bjsxt.model.OrderBooks;
import com.lmax.disruptor.EventHandler;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * 该对象有多个，与交易对一一对应
 * 针对某一个交易对，同一时间只会有一个线程来执行
 */
@Data
@Slf4j
public class OrderEventHandler implements EventHandler<OrderEvent> {

    private OrderBooks orderBooks;

    private String symbol ;

    public OrderEventHandler(OrderBooks orderBooks) {
        this.orderBooks = orderBooks;
        this.symbol =  this.orderBooks.getSymbol() ;
    }
    /**
     * 接收到了某个消息（从ringbuffer里接收了某个数据）
     *
     * @param event
     * @param sequence
     * @param endOfBatch
     * @throws Exception
     */
    @Override
    public void onEvent(OrderEvent event, long sequence, boolean endOfBatch) throws Exception {
        // 从ringbuffer 里面接收了某个数据
        Order order = (Order)event.getSource();
        if(!order.getSymbol().equals(symbol)){ // 我们接收到了一个不属于我们处理的数据,我们不处理
            return;
        }

        log.info("开始接收订单事件============>{}", event);

        MatchServiceFactory.getMatchService(MatchStrategy.LIMIT_PRICE).match(orderBooks ,order);

        /// 处理逻辑是啥?
        log.info("处理完成我们的订单事件===================>{}", event);
    }
}
