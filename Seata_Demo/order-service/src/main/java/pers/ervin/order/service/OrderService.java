package pers.ervin.order.service;

import pers.ervin.order.entity.Order;

public interface OrderService {

    /**
     * 创建订单
     */
    Long create(Order order);
}