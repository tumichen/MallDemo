package com.jt.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jt.mapper.OrderItemMapper;
import com.jt.mapper.OrderMapper;
import com.jt.mapper.OrderShippingMapper;
import com.jt.pojo.Order;
import com.jt.pojo.OrderItem;
import com.jt.pojo.OrderShipping;
import com.jt.service.DubboOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Date;
import java.util.List;

@Service
public class DubboOrderServiceImpl implements DubboOrderService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderShippingMapper orderShippingMapper;
    @Autowired
    private OrderItemMapper orderItemMapper;

    @Override
    @Transactional
    public String saveOrder(Order order) {
        String orderId = Instant.now().toEpochMilli()+""+order.getUserId();
        Date now = new Date();
        order.setOrderId(orderId).setStatus(1).setCreated(now).setUpdated(now);
        orderMapper.insert(order);
        OrderShipping orderShipping = order.getOrderShipping();
        orderShipping.setOrderId(orderId).setCreated(now).setUpdated(now);
        orderShippingMapper.insert(orderShipping);
        List<OrderItem> orderItems = order.getOrderItems();
        orderItems.stream().forEach(orderItem -> {
            orderItem.setOrderId(orderId).setCreated(now).setUpdated(now);
            orderItemMapper.insert(orderItem);
        });
        return orderId;
    }

    @Override
    public Order findById(String id) {
        Order order = orderMapper.selectById(id);
        OrderShipping shipping = orderShippingMapper.selectById(id);
        QueryWrapper<OrderItem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_id",id);
        List<OrderItem> items =
                orderItemMapper.selectList(queryWrapper);
        order.setOrderItems(items)
                .setOrderShipping(shipping);
        return order;
    }
}
