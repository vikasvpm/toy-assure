package org.learning.assure.api.flow;

import org.learning.assure.api.OrderApi;
import org.learning.assure.exception.ApiException;
import org.learning.assure.pojo.OrderItemPojo;
import org.learning.assure.pojo.OrderPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

@Service
@Transactional(rollbackFor = ApiException.class)
public class OrderAndOrderItemsFlowApi {

    @Autowired
    private OrderApi orderApi;
    public void createOrderAndItems(OrderPojo orderPojo, List<OrderItemPojo> orderItemPojoList) {
        orderApi.createOrder(orderPojo);
        orderApi.createOrderItem(orderItemPojoList);

    }
}
