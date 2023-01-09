package org.learning.assure.api;

import org.learning.assure.dao.OrderDao;
import org.learning.assure.pojo.OrderItemPojo;
import org.learning.assure.pojo.OrderPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class OrderApi {

    @Autowired
    private OrderDao orderDao;
    public OrderPojo createInternalOrder(OrderPojo orderPojo) {
        OrderPojo createdOrderPojo = orderDao.createInternalOrder(orderPojo);
        return createdOrderPojo;
    }

    public void createInternalOrderItem(List<OrderItemPojo> orderItemPojoList) {
        for(OrderItemPojo orderItemPojo : orderItemPojoList) {
            orderDao.createInternalOrderItem(orderItemPojo);
        }
    }
}
