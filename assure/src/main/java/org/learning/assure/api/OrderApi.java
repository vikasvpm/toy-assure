package org.learning.assure.api;

import org.learning.assure.dao.OrderDao;
import org.learning.assure.model.enums.OrderStatus;
import org.learning.assure.pojo.OrderItemPojo;
import org.learning.assure.pojo.OrderPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class OrderApi {

    @Autowired
    private OrderDao orderDao;
    public OrderPojo createOrder(OrderPojo orderPojo) {
        OrderPojo createdOrderPojo = orderDao.createOrder(orderPojo);
        return createdOrderPojo;
    }

    public void createOrderItem(List<OrderItemPojo> orderItemPojoList) {
        for(OrderItemPojo orderItemPojo : orderItemPojoList) {
            orderDao.createOrderItem(orderItemPojo);
        }
    }

    @Transactional(readOnly = true)
    public OrderPojo getOrderByChannelOrder(String channelOrderId, Long channelId) {
        return orderDao.getOrderByChannelOrder(channelOrderId, channelId);

    }

    @Transactional(readOnly = true)
    public List<OrderPojo> getOrdersByStatus(OrderStatus status) {
        return orderDao.getOrderByStatus(status);
    }

    @Transactional(readOnly = true)
    public List<OrderItemPojo> getOrderItemsByOrderId(Long orderId) {
        return orderDao.getOrderItemsByOrderId(orderId);
    }

    public OrderPojo createOrderAndOrderItems(OrderPojo orderPojo, List<OrderItemPojo> orderItemPojoList) {
        OrderPojo createdOrder = orderDao.createOrder(orderPojo);
        for(OrderItemPojo orderItemPojo : orderItemPojoList) {
            orderItemPojo.setOrderId(createdOrder.getOrderId());
            orderDao.createOrderItem(orderItemPojo);
        }
        return createdOrder;
    }

    @Transactional(readOnly = true)
    public OrderPojo getOrderByOrderId(Long orderId) {
        return orderDao.getOrderByOrderId(orderId);
    }
}
