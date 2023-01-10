package org.learning.assure.dto.helper;

import lombok.Data;
import org.learning.assure.model.form.ChannelOrderForm;
import org.learning.assure.model.form.InternalOrderForm;
import org.learning.assure.pojo.OrderItemPojo;
import org.learning.assure.pojo.OrderPojo;
import org.learning.assure.pojo.OrderStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
public class OrderHelper {

    public static OrderPojo convertToInternalOrder(String channelOrderId, Long clientId, Long customerId) {
        OrderPojo orderPojo = new OrderPojo();
        orderPojo.setChannelOrderId(channelOrderId);
        orderPojo.setClientId(clientId);
        orderPojo.setCustomerId(customerId);
        orderPojo.setOrderStatus(OrderStatus.CREATED);
        orderPojo.setChannelId(1l);
        return orderPojo;
    }

    public static List<OrderItemPojo> convertToInternalOrderItemList(Long orderId, List<InternalOrderForm> internalOrderFormList, Map<String, Long> map) {
        List<OrderItemPojo> orderItemPojoList = new ArrayList<>();
        for(InternalOrderForm internalOrderForm : internalOrderFormList) {
            OrderItemPojo orderItemPojo = new OrderItemPojo();
            orderItemPojo.setOrderId(orderId);
            orderItemPojo.setOrderedQuantity(internalOrderForm.getOrderedQuantity());
            orderItemPojo.setSellingPricePerUnit(internalOrderForm.getSellingPricePerUnit());
            orderItemPojo.setGlobalSkuId(map.get(internalOrderForm.getClientSkuId()));
            orderItemPojo.setFulfilledQuantity(0l);
            orderItemPojo.setAllocatedQuantity(0l);
            orderItemPojoList.add(orderItemPojo);
        }
        return orderItemPojoList;
    }

    public static OrderPojo createChannelOrder(Long channelId, Long clientId, Long customerId, String channelOrderId) {
        OrderPojo orderPojo = new OrderPojo();
        orderPojo.setClientId(clientId);
        orderPojo.setCustomerId(customerId);
        orderPojo.setChannelOrderId(channelOrderId);
        orderPojo.setChannelId(channelId);
        orderPojo.setOrderStatus(OrderStatus.CREATED);
        return orderPojo;
    }

    public static List<OrderItemPojo> createChannelOrderItem(Long orderId, Map<String, Long> map, List<ChannelOrderForm> channelOrderFormList) {
        List<OrderItemPojo> orderItemPojoList = new ArrayList<>();
        for(ChannelOrderForm channelOrderForm : channelOrderFormList) {
            OrderItemPojo orderItemPojo = new OrderItemPojo();
            orderItemPojo.setOrderId(orderId);
            orderItemPojo.setOrderedQuantity(channelOrderForm.getOrderedQuantity());
            orderItemPojo.setSellingPricePerUnit(channelOrderForm.getSellingPricePerUnit());
            orderItemPojo.setGlobalSkuId(map.get(channelOrderForm.getChannelSkuId()));
            orderItemPojo.setFulfilledQuantity(0l);
            orderItemPojo.setAllocatedQuantity(0l);
            orderItemPojoList.add(orderItemPojo);
        }
        return orderItemPojoList;
    }
}