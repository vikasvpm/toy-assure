package org.learning.assure.dto;

import org.learning.assure.api.OrderApi;
import org.learning.assure.api.ProductApi;
import org.learning.assure.api.UserApi;
import org.learning.assure.dto.helper.InternalOrderHelper;
import org.learning.assure.exception.ApiException;
import org.learning.assure.model.form.InternalOrderForm;
import org.learning.assure.pojo.OrderItemPojo;
import org.learning.assure.pojo.OrderPojo;
import org.learning.assure.pojo.ProductPojo;
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
    public void createInternalOrder(List<InternalOrderForm> internalOrderFormList, Long clientId, String channelOrderId, Long customerId) throws ApiException {
        validateClient(clientId);
        validateCustomer(customerId);
        validateClientSkus(internalOrderFormList, clientId);
        validateChannelOrderId(channelOrderId);
        OrderPojo orderPojo = InternalOrderHelper.convertToInternalOrder(channelOrderId, clientId, customerId);
        OrderPojo createdOrder = orderApi.createInternalOrder(orderPojo);
        Map<String, Long> map = mapToGlobalSkuId(internalOrderFormList, clientId);
        List<OrderItemPojo> orderItemPojoList = InternalOrderHelper.convertToInternalOrderItemList(
                createdOrder.getOrderId(), internalOrderFormList, map);
        orderApi.createInternalOrderItem(orderItemPojoList);

    }

    private Map<String, Long> mapToGlobalSkuId(List<InternalOrderForm> internalOrderFormList, Long clientId) {
        Map<String, Long> map = new HashMap<>();
        for(InternalOrderForm internalOrderForm : internalOrderFormList) {
            ProductPojo productPojo = productApi.getProductByClientIdAndClientSkuId(clientId, internalOrderForm.getClientSkuId());
            map.put(internalOrderForm.getClientSkuId(), productPojo.getGlobalSkuId());
        }
        return map;
    }

    private void validateChannelOrderId(String channelOrderId) {
        // TODO : Add this validation
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
}
