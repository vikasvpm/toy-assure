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
        OrderPojo createdOrderPojo = orderDao.createInternalOrder(orderPojo);
        return createdOrderPojo;
    }

    public void createOrderItem(List<OrderItemPojo> orderItemPojoList) {
        for(OrderItemPojo orderItemPojo : orderItemPojoList) {
            orderDao.createInternalOrderItem(orderItemPojo);
        }
    }

    public OrderPojo getOrderByChannelOrder(String channelOrderId, Long channelId) {
        return orderDao.getOrderByChannelOrder(channelOrderId, channelId);

    }

    public List<OrderPojo> getOrdersByStatus(OrderStatus status) {
        return orderDao.getOrderByStatus(status);
    }

    public List<OrderItemPojo> getOrderItemsByOrderId(Long orderId) {
        return orderDao.getOrderItemsByOrderId(orderId);
    }
}
