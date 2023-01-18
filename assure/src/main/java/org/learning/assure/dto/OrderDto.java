package org.learning.assure.dto;

import org.learning.assure.api.*;
import org.learning.assure.api.flow.AllocateOrderFlowApi;
import org.learning.assure.api.flow.OrderAndOrderItemsFlowApi;
import org.learning.assure.dto.helper.OrderHelper;
import org.learning.assure.exception.ApiException;
import org.learning.assure.model.enums.OrderStatus;
import org.learning.assure.model.form.ChannelOrderForm;
import org.learning.assure.model.form.InternalOrderForm;
import org.learning.assure.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Service
public class OrderDto {

    @Autowired
    private ProductApi productApi;
    @Autowired
    private InventoryApi inventoryApi;

    @Autowired
    private UserApi userApi;

    @Autowired
    private OrderApi orderApi;

    @Autowired
    private ChannelApi channelApi;

    @Autowired
    private BinSkuApi binSkuApi;

    @Autowired
    private ChannelListingApi channelListingApi;
    @Autowired
    private OrderAndOrderItemsFlowApi orderAndOrderItemsFlowApi;

    @Autowired
    private AllocateOrderFlowApi allocateOrderFlowApi;
    public void createInternalOrder(List<InternalOrderForm> internalOrderFormList, Long clientId, String channelOrderId, Long customerId) throws ApiException {
        validateClient(clientId);
        validateCustomer(customerId);
        validateClientSkus(internalOrderFormList, clientId);
        validateChannelOrderId(channelOrderId, 1l);
        validateInternalOrderedQuantity(internalOrderFormList);
        OrderPojo orderPojo = OrderHelper.convertToInternalOrder(channelOrderId, clientId, customerId);
        Map<String, Long> map = mapClientSkuIdToGlobalSkuId(internalOrderFormList, clientId);
        List<OrderItemPojo> orderItemPojoList = OrderHelper.convertToInternalOrderItemList(internalOrderFormList, map);
        orderApi.createOrderAndOrderItems(orderPojo, orderItemPojoList);
    }

    private void validateInternalOrderedQuantity(List<InternalOrderForm> internalOrderFormList) throws ApiException {
        for(InternalOrderForm internalOrderForm : internalOrderFormList) {
            if(internalOrderForm.getOrderedQuantity() < 1) {
                throw new ApiException("Ordered quantity can not be 0 or negative, such value found for product with client SKU ID = " + internalOrderForm.getClientSkuId());
            }
        }
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
        if(!Objects.isNull(orderPojo)) {
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
                            throw new ApiException("Duplicate Client SKU ID " + clientSkuId + " is present in the upload");
                        } catch (ApiException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    clientSkuIdSet.add(clientSkuId);
                    ProductPojo productPojo = productApi.getProductByClientIdAndClientSkuId(clientId, clientSkuId);
                    if(Objects.isNull(productPojo)) {
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
        validateChannelOrderedQuantity(channelOrderFormList);
        validateChannelOrderId(channelOrderId, channelPojo.getChannelId());
        OrderPojo orderPojo = OrderHelper.convertToChannelOrder(channelPojo.getChannelId(), clientId, customerId, channelOrderId);
        Map<String, Long> map = mapChannelSkuIdToGlobalSkuId(channelOrderFormList, channelPojo.getChannelId(), clientId);
        List<OrderItemPojo> orderItemPojoList = OrderHelper.convertToChannelOrderItem(map, channelOrderFormList);
        orderApi.createOrderAndOrderItems(orderPojo, orderItemPojoList);
    }

    private void validateChannelOrderedQuantity(List<ChannelOrderForm> channelOrderFormList) throws ApiException {
        for(ChannelOrderForm channelOrderForm : channelOrderFormList) {
            if(channelOrderForm.getOrderedQuantity() < 1) {
                throw new ApiException("Ordered quantity can not be 0 or negative, such value found for product with channel SKU ID = " + channelOrderForm.getChannelSkuId());
            }
        }
    }

    private void validateChannelSkuIds(List<ChannelOrderForm> channelOrderFormList, Long clientId, Long channelId) {
        channelOrderFormList.stream().map(ChannelOrderForm::getChannelSkuId)
                .forEach(channelSkuId -> {
                    ChannelListingPojo channelListingPojo = channelListingApi.getChannelListingToMapGlobalSkuId(clientId, channelId, channelSkuId);
                    if(Objects.isNull(channelListingPojo)) {
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
        if(Objects.isNull(channelPojo)) {
            throw new ApiException("No Channel exists with Channel Name " + channelName);
        }
    }

    @Transactional
    public void allocateOrder() {
        List<OrderPojo> createdOrders = orderApi.getOrdersByStatus(OrderStatus.CREATED);
        for(OrderPojo orderPojo : createdOrders) {
            Boolean completeAllocate = true;
            List<OrderItemPojo> orderedItems = orderApi.getOrderItemsByOrderId(orderPojo.getOrderId());
            for(OrderItemPojo orderItemPojo : orderedItems) {
                if(orderItemPojo.getOrderedQuantity() > orderItemPojo.getAllocatedQuantity()) {
                    InventoryPojo inventoryPojo = inventoryApi.getByGlobalSkuId(orderItemPojo.getGlobalSkuId());
                    Long allocated;
                    if(inventoryPojo.getAvailableQuantity() >= orderItemPojo.getOrderedQuantity() - orderItemPojo.getAllocatedQuantity()) {
                        allocated = orderItemPojo.getOrderedQuantity() - orderItemPojo.getAllocatedQuantity();
                    }
                    else {
                        allocated = inventoryPojo.getAvailableQuantity();
                        completeAllocate = false;
                    }
                    inventoryPojo.setAvailableQuantity(inventoryPojo.getAvailableQuantity() - allocated);
                    inventoryPojo.setAllocatedQuantity(inventoryPojo.getAllocatedQuantity() + allocated);
                    orderItemPojo.setAllocatedQuantity(orderItemPojo.getAllocatedQuantity() + allocated);
                    List<BinSkuPojo> binSkuPojoList = binSkuApi.getListByGlobalSkuId(orderItemPojo.getGlobalSkuId());
                    for(BinSkuPojo binSkuPojo : binSkuPojoList) {
                        if(binSkuPojo.getQuantity() > allocated) {
                            binSkuPojo.setQuantity(binSkuPojo.getQuantity() - allocated);
                            break;
                        }
                        else {
                            allocated = allocated - binSkuPojo.getQuantity();
                            binSkuApi.deleteByBinSkuId(binSkuPojo.getBinSkuId());
                        }
                    }
                }
            }
            if(completeAllocate.equals(true)) {
                orderPojo.setOrderStatus(OrderStatus.ALLOCATED);
            }
        }
    }
}
