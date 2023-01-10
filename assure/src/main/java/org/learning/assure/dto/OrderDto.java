package org.learning.assure.dto;

import org.learning.assure.api.*;
import org.learning.assure.dto.helper.OrderHelper;
import org.learning.assure.exception.ApiException;
import org.learning.assure.model.form.ChannelOrderForm;
import org.learning.assure.model.form.InternalOrderForm;
import org.learning.assure.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OrderDto {

    @Autowired
    private ProductApi productApi;

    @Autowired
    private UserApi userApi;

    @Autowired
    private OrderApi orderApi;

    @Autowired
    private ChannelApi channelApi;

    @Autowired
    private ChannelListingApi channelListingApi;
    public void createInternalOrder(List<InternalOrderForm> internalOrderFormList, Long clientId, String channelOrderId, Long customerId) throws ApiException {
        validateClient(clientId);
        validateCustomer(customerId);
        validateClientSkus(internalOrderFormList, clientId);
        validateChannelOrderId(channelOrderId, 1l);
        OrderPojo orderPojo = OrderHelper.convertToInternalOrder(channelOrderId, clientId, customerId);
        OrderPojo createdOrder = orderApi.createOrder(orderPojo);
        Map<String, Long> map = mapClientSkuIdToGlobalSkuId(internalOrderFormList, clientId);
        List<OrderItemPojo> orderItemPojoList = OrderHelper.convertToInternalOrderItemList(
                createdOrder.getOrderId(), internalOrderFormList, map);
        orderApi.createOrderItem(orderItemPojoList);

    }

    private Map<String, Long> mapClientSkuIdToGlobalSkuId(List<InternalOrderForm> internalOrderFormList, Long clientId) {
        Map<String, Long> map = new HashMap<>();
        for(InternalOrderForm internalOrderForm : internalOrderFormList) {
            ProductPojo productPojo = productApi.getProductByClientIdAndClientSkuId(clientId, internalOrderForm.getClientSkuId());
            map.put(internalOrderForm.getClientSkuId(), productPojo.getGlobalSkuId());
        }
        return map;
    }

    private void validateChannelOrderId(String channelOrderId, Long channelId) throws ApiException {
        OrderPojo orderPojo = orderApi.getOrderByChannelOrder(channelOrderId, channelId);
        if(orderPojo != null) {
            throw new ApiException("ChannelOrderId " + channelOrderId + " already exists for the Channel " + channelId );
        }

    }

    private void validateCustomer(Long customerId) throws ApiException {
        userApi.invalidCustomerCheck(customerId);
    }

    private void validateClient(Long clientId) throws ApiException {
        userApi.invalidClientCheck(clientId);
    }

    private void validateClientSkus(List<InternalOrderForm> internalOrderFormList, Long clientId) {
        Set<String> clientSkuIdSet = new HashSet<>();
        internalOrderFormList.stream().map(InternalOrderForm::getClientSkuId)
                .forEach(clientSkuId -> {
                    if(clientSkuIdSet.contains(clientSkuId)) {
                        try {
                            throw new ApiException("Duplicate Client SKU ID is present");
                        } catch (ApiException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    clientSkuIdSet.add(clientSkuId);
                    ProductPojo productPojo = productApi.getProductByClientIdAndClientSkuId(clientId, clientSkuId);
                    if(productPojo == null) {
                        try {
                            throw new ApiException("Product with Client SKU ID " + clientSkuId + " is not present in the system for client with ID " + clientId );
                        } catch (ApiException e) {
                            throw new RuntimeException(e);
                        }
                    }

                });
    }

    public void createChannelOrder(List<ChannelOrderForm> channelOrderFormList, Long clientId, String channelOrderId, Long customerId, String channelName) throws ApiException {
        validateClient(clientId);
        validateCustomer(customerId);
        validateChannelName(channelName);
        ChannelPojo channelPojo = channelApi.getChannelByName(channelName);
        validateChannelSkuIds(channelOrderFormList, clientId, channelPojo.getChannelId());
        validateChannelOrderId(channelOrderId, channelPojo.getChannelId());
        OrderPojo orderPojo = OrderHelper.createChannelOrder(channelPojo.getChannelId(), clientId, customerId, channelOrderId);
        OrderPojo createdOrder = orderApi.createOrder(orderPojo);
        Map<String, Long> map = mapChannelSkuIdToGlobalSkuId(channelOrderFormList, channelPojo.getChannelId(), clientId);
        List<OrderItemPojo> orderItemPojoList = OrderHelper.createChannelOrderItem(createdOrder.getOrderId(),map, channelOrderFormList);
        orderApi.createOrderItem(orderItemPojoList);
    }

    private void validateChannelSkuIds(List<ChannelOrderForm> channelOrderFormList, Long clientId, Long channelId) {
        channelOrderFormList.stream().map(ChannelOrderForm::getChannelSkuId)
                .forEach(channelSkuId -> {
                    ChannelListingPojo channelListingPojo = channelListingApi.getChannelListingToMapGlobalSkuId(clientId, channelId, channelSkuId);
                    if(channelListingPojo == null) {
                        try {
                            throw new ApiException("No product with channelSkuId = " + channelSkuId + " present for client " + clientId + " and channel " + channelId);
                        } catch (ApiException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
    }


    private Map<String, Long> mapChannelSkuIdToGlobalSkuId(List<ChannelOrderForm> channelOrderFormList, Long channelId, Long clientId) {
        Map<String, Long> map = new HashMap<>();
        for(ChannelOrderForm channelOrderForm : channelOrderFormList) {
            ChannelListingPojo channelListingPojo = channelListingApi.getChannelListingToMapGlobalSkuId(clientId, channelId, channelOrderForm.getChannelSkuId());
            map.put(channelOrderForm.getChannelSkuId(), channelListingPojo.getGlobalSkuId());
        }
        return map;
    }

    private void validateChannelName(String channelName) throws ApiException {
        if(channelName.equals("INTERNAL")) {
            throw new ApiException("Channel name can not be 'INTERNAL' for channel orders");
        }
        ChannelPojo channelPojo = channelApi.getChannelByName(channelName);
        if(channelPojo == null) {
            throw new ApiException("No Channel exists with Channel Name " + channelName);
        }
    }
}
