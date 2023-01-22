package org.learning.assure.dto;

import org.apache.commons.io.FilenameUtils;
import org.learning.assure.api.*;
import org.learning.assure.api.flow.AllocateOrderFlowApi;
import org.learning.assure.api.flow.OrderAndOrderItemsFlowApi;
import org.learning.assure.dto.helper.OrderHelper;
import org.learning.assure.exception.ApiException;
import org.learning.assure.model.enums.OrderStatus;
import org.learning.assure.model.form.ChannelOrderForm;
import org.learning.assure.model.form.InternalOrderForm;
import org.learning.assure.pojo.*;
import org.learning.assure.util.csvParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
    @Autowired
    private InvoiceApi invoiceApi;
    public void createInternalOrder(MultipartFile internalOrderCsv, Long clientId, String channelOrderId, Long customerId) throws ApiException, IOException {
        validateClient(clientId);
        validateCustomer(customerId);
        validateChannelOrderId(channelOrderId, 1l);
        List<InternalOrderForm> internalOrderFormList = parseInteralOrderCsv(internalOrderCsv);
        validateClientSkus(internalOrderFormList, clientId);
        validateInternalOrderedQuantity(internalOrderFormList);
        OrderPojo orderPojo = OrderHelper.convertToInternalOrder(channelOrderId, clientId, customerId);
        Map<String, Long> map = mapClientSkuIdToGlobalSkuId(internalOrderFormList, clientId);
        List<OrderItemPojo> orderItemPojoList = OrderHelper.convertToInternalOrderItemList(internalOrderFormList, map);
        orderApi.createOrderAndOrderItems(orderPojo, orderItemPojoList);
    }

    private List<InternalOrderForm> parseInteralOrderCsv(MultipartFile internalOrderCsvFile) throws ApiException, IOException {
        if (!FilenameUtils.isExtension(internalOrderCsvFile.getOriginalFilename(), "csv")) {
            throw new ApiException("Input file is not a valid CSV file");
        }
        List<InternalOrderForm> internalOrderFormList = csvParser.parseCSV(internalOrderCsvFile.getBytes(), InternalOrderForm.class);
        return internalOrderFormList;
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

    public void createChannelOrder(@RequestBody MultipartFile channelOrderCsv, Long clientId, String channelOrderId, Long customerId, String channelName) throws ApiException, IOException {
        validateClient(clientId);
        validateCustomer(customerId);
        validateChannelName(channelName);
        ChannelPojo channelPojo = channelApi.getChannelByName(channelName);
        validateChannelOrderId(channelOrderId, channelPojo.getChannelId());
        List<ChannelOrderForm> channelOrderFormList = parseChannelOrderCsv(channelOrderCsv);
        validateChannelSkuIds(channelOrderFormList, clientId, channelPojo.getChannelId());
        validateChannelOrderedQuantity(channelOrderFormList);

        OrderPojo orderPojo = OrderHelper.convertToChannelOrder(channelPojo.getChannelId(), clientId, customerId, channelOrderId);
        Map<String, Long> map = mapChannelSkuIdToGlobalSkuId(channelOrderFormList, channelPojo.getChannelId(), clientId);
        List<OrderItemPojo> orderItemPojoList = OrderHelper.convertToChannelOrderItem(map, channelOrderFormList);
        orderApi.createOrderAndOrderItems(orderPojo, orderItemPojoList);
    }

    private List<ChannelOrderForm> parseChannelOrderCsv(MultipartFile channelOrderCsv) throws ApiException, IOException {
        if (!FilenameUtils.isExtension(channelOrderCsv.getOriginalFilename(), "csv")) {
            throw new ApiException("Input file is not a valid CSV file");
        }
        List<ChannelOrderForm> channelOrderFormList = csvParser.parseCSV(channelOrderCsv.getBytes(), ChannelOrderForm.class);
        return channelOrderFormList;
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

    public void allocateOrder(Long orderId) throws ApiException {
        validateOrderForAllocation(orderId);
        allocateOrderFlowApi.allocateOrder(orderId);
    }

    private void validateOrderForAllocation(Long orderId) throws ApiException {
        OrderPojo orderPojo = orderApi.getOrderByOrderId(orderId);
        if(Objects.isNull(orderPojo)) {
            throw new ApiException("No order with ID = " + orderId + " exists in the system");
        }
        else if(!orderPojo.getOrderStatus().equals(OrderStatus.CREATED)) {
            throw new ApiException("Order with ID = " + orderId + " is not in CREATED state and can not be allocated");
        }
    }

    public void fulfillOrder(Long orderId, HttpServletResponse response) throws ApiException {
        validateOrderForFulfillment(orderId);
        OrderPojo orderPojo = orderApi.getOrderByOrderId(orderId);
        String fileName = "Invoice_" + orderPojo.getChannelOrderId();
        List<OrderItemPojo> orderItemPojoList = orderApi.getOrderItemsByOrderId(orderId);
        try {
            byte[] invoiceBytes = invoiceApi.generateInvoice(orderItemPojoList, orderPojo);
            response.setContentType("application/pdf");
            response.addHeader("Content-Disposition", "attachment; filename=" + fileName);
            response.setContentLengthLong(invoiceBytes.length);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            baos.write(invoiceBytes);
            baos.writeTo(response.getOutputStream());
            baos.close();
        }
        catch (Exception ex) {
            throw new ApiException("Error generating invoice");
        }

    }
    private void validateOrderForFulfillment(Long orderId) throws ApiException {
        OrderPojo orderPojo = orderApi.getOrderByOrderId(orderId);
        if(Objects.isNull(orderPojo)) {
            throw new ApiException("No order with ID = " + orderId + " exists in the system");
        }
        else if(!orderPojo.getOrderStatus().equals(OrderStatus.ALLOCATED)) {
            throw new ApiException("Order with ID = " + orderId + " is not in ALLOCATED state and can not be fulfilled");
        }
    }
}
