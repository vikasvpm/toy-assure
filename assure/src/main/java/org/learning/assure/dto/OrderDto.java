package org.learning.assure.dto;

import org.apache.commons.io.FilenameUtils;
import org.learning.assure.api.*;
import org.learning.assure.api.flow.AllocateOrderFlowApi;
import org.learning.assure.api.flow.InvoiceFlowApi;
import org.learning.assure.api.flow.OrderAndOrderItemsFlowApi;
import org.learning.assure.dto.helper.OrderHelper;
import org.learning.assure.dto.helper.ThrowExceptionHelper;
import org.learning.assure.exception.ApiException;
import org.learning.assure.model.enums.OrderStatus;
import org.learning.assure.model.form.ChannelListingForm;
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
import java.nio.file.Files;
import java.nio.file.Paths;
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
    private InvoiceFlowApi invoiceFlowApi;
    public OrderPojo createInternalOrder(MultipartFile internalOrderCsv, Long clientId, String channelOrderId, Long customerId) throws ApiException, IOException {
        validateInteralOrderNullValues(clientId, channelOrderId, customerId);
        validateClient(clientId);
        validateCustomer(customerId);
        validateInternalChannelExists();
        ChannelPojo internalChannel = channelApi.getChannelByName("INTERNAL");
        validateChannelOrderId(channelOrderId, internalChannel.getChannelId());
        List<InternalOrderForm> internalOrderFormList = parseInteralOrderCsv(internalOrderCsv);
        List<String> errorList = new ArrayList<>();
        validateInternalOrderForm(internalOrderFormList, clientId, errorList);
        OrderPojo orderPojo = OrderHelper.convertToInternalOrder(channelOrderId, clientId, customerId);
        Map<String, Long> map = mapClientSkuIdToGlobalSkuId(internalOrderFormList, clientId);
        List<OrderItemPojo> orderItemPojoList = OrderHelper.convertToInternalOrderItemList(internalOrderFormList, map);
        return orderApi.createOrderAndOrderItems(orderPojo, orderItemPojoList);
    }

    private void validateInteralOrderNullValues(Long clientId, String channelOrderId, Long customerId) throws ApiException {
        List<String> errors = new ArrayList<>();
        if(Objects.isNull(clientId)) {
            errors.add("Client ID can not be null");
        }
        if(Objects.isNull(customerId)) {
            errors.add("Customer ID can not be null");
        }
        if(Objects.isNull(channelOrderId) || channelOrderId.equals("")) {
            errors.add("Channel Order ID can not be null or blank");
        }
        ThrowExceptionHelper.throwIfErrors(errors);
    }

    private void validateInternalChannelExists() throws ApiException {
        if(Objects.isNull(channelApi.getChannelByName("INTERNAL"))) {
            throw new ApiException("INTERNAL channel does not exist");
        }

    }

    private List<InternalOrderForm> parseInteralOrderCsv(MultipartFile internalOrderCsvFile) throws ApiException, IOException {
        if (!FilenameUtils.isExtension(internalOrderCsvFile.getOriginalFilename(), "csv")) {
            throw new ApiException("Input file is not a valid CSV file");
        }
        List<InternalOrderForm> internalOrderFormList = csvParser.parseCSV(internalOrderCsvFile.getBytes(), InternalOrderForm.class);
        return internalOrderFormList;
    }

    private Map<String, Long> mapClientSkuIdToGlobalSkuId(List<InternalOrderForm> internalOrderFormList, Long clientId) {
        Map<String, Long> map = new HashMap<>();
        for(InternalOrderForm internalOrderForm : internalOrderFormList) {
            ProductPojo productPojo = productApi.getProductByClientIdAndClientSkuId(clientId, internalOrderForm.getClientSkuId());
            map.put(internalOrderForm.getClientSkuId(), productPojo.getGlobalSkuId());
        }
        return map;
    }

    private void validateInternalOrderForm(List<InternalOrderForm> internalOrderFormList, Long clientId, List<String> errorList) throws ApiException {
        Set<String> clientSkuIdSet = new HashSet<>();
        for(InternalOrderForm internalOrderForm: internalOrderFormList) {
            String clientSkuId = internalOrderForm.getClientSkuId();
            ProductPojo productPojo = productApi.getProductByClientIdAndClientSkuId(clientId, clientSkuId);
            if(Objects.isNull(productPojo)) {
                errorList.add("Product with Client SKU ID " + clientSkuId + " is not present in the system for client with ID " + clientId);
            }
            else if(clientSkuIdSet.contains(clientSkuId)) {
                errorList.add("Duplicate Client SKU ID " + clientSkuId + " is present in the upload");
            }
            else {
                clientSkuIdSet.add(clientSkuId);
                if(internalOrderForm.getOrderedQuantity() < 1) {
                    errorList.add("Ordered quantity can not be 0 or negative: such value found for product with client SKU ID = " + clientSkuId);
                }
            }
        }
        if(!errorList.isEmpty()) {
            ThrowExceptionHelper.throwIfErrors(errorList);
        }
    }

    private void validateCustomer(Long customerId) throws ApiException {
        userApi.invalidCustomerCheck(customerId);
    }

    public void validateClient(Long clientId) throws ApiException {
        List<String> errorList = new ArrayList<>();
        userApi.invalidClientCheck(clientId);
    }

    public OrderPojo createChannelOrder(@RequestBody MultipartFile channelOrderCsv, Long clientId, String channelOrderId, Long customerId, String channelName) throws ApiException, IOException {
        validateChannelOrderNullValues(clientId, customerId, channelOrderId, channelName);
        validateClient(clientId);
        validateCustomer(customerId);
        validateChannelName(channelName);
        ChannelPojo channelPojo = channelApi.getChannelByName(channelName);
        validateChannelOrderId(channelOrderId, channelPojo.getChannelId());
        List<ChannelOrderForm> channelOrderFormList = parseChannelOrderCsv(channelOrderCsv);
        List<String> errorList = new ArrayList<>();
        validateChannelOrderForm(channelOrderFormList, clientId, channelPojo.getChannelId(), errorList);
        OrderPojo orderPojo = OrderHelper.convertToChannelOrder(channelPojo.getChannelId(), clientId, customerId, channelOrderId);
        Map<String, Long> map = mapChannelSkuIdToGlobalSkuId(channelOrderFormList, channelPojo.getChannelId(), clientId);
        List<OrderItemPojo> orderItemPojoList = OrderHelper.convertToChannelOrderItem(map, channelOrderFormList);
        return orderApi.createOrderAndOrderItems(orderPojo, orderItemPojoList);
    }

    private void validateChannelOrderNullValues(Long clientId, Long customerId, String channelOrderId, String channelName) throws ApiException {
        List<String> errors = new ArrayList<>();
        if(Objects.isNull(clientId)) {
            errors.add("Client ID can not be null");
        }
        if(Objects.isNull(customerId)) {
            errors.add("Customer ID can not be null");
        }
        if(Objects.isNull(channelOrderId) || channelOrderId.equals("")) {
            errors.add("Channel Order ID can not be null or blank");
        }
        if(Objects.isNull(channelName) || channelName.equals("")) {
            errors.add("Channel Name can not be null or blank");
        }
        ThrowExceptionHelper.throwIfErrors(errors);
    }

    private List<ChannelOrderForm> parseChannelOrderCsv(MultipartFile channelOrderCsv) throws ApiException, IOException {
        if (!FilenameUtils.isExtension(channelOrderCsv.getOriginalFilename(), "csv")) {
            throw new ApiException("Input file is not a valid CSV file");
        }
        List<ChannelOrderForm> channelOrderFormList = csvParser.parseCSV(channelOrderCsv.getBytes(), ChannelOrderForm.class);
        return channelOrderFormList;
    }

    private void validateChannelOrderId(String channelOrderId, Long channelId) throws ApiException {
        OrderPojo orderPojo = orderApi.getOrderByChannelOrder(channelOrderId, channelId);
        if(!Objects.isNull(orderPojo)) {
            throw new ApiException("Channel Order Id " + channelOrderId + " already exists for the Channel " + channelId );
        }
    }

    private void validateChannelOrderForm(List<ChannelOrderForm> channelOrderFormList, Long clientId, Long channelId, List<String> errorList) throws ApiException {
        for(ChannelOrderForm channelOrderForm : channelOrderFormList) {
            String channelSkuId = channelOrderForm.getChannelSkuId();
            ChannelListingPojo channelListingPojo = channelListingApi.getChannelListingToMapGlobalSkuId(clientId,channelId,channelSkuId);
            if(Objects.isNull(channelListingPojo)) {
                errorList.add("No product with channelSkuId = " + channelSkuId + " present for client " + clientId + " and channel " + channelId);
            }
            else if(channelOrderForm.getOrderedQuantity() < 1L) {
                errorList.add("Ordered quantity can not be 0 or negative: such value found for product with channel SKU ID = " + channelSkuId);
            }
        }
        if(!errorList.isEmpty()) {
            ThrowExceptionHelper.throwIfErrors(errorList);
        }
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
            throw new ApiException("No Channel exists with Channel Name = " + channelName);
        }
    }

    public OrderPojo allocateOrder(Long orderId) throws ApiException {
        validateOrderForAllocation(orderId);
        return allocateOrderFlowApi.allocateOrder(orderId);
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
    public OrderPojo fulfillOrder(Long orderId, HttpServletResponse response) throws ApiException {
        validateOrderForFulfillment(orderId);
        OrderPojo orderPojo = orderApi.getOrderByOrderId(orderId);
        String fileName = "Invoice_" + orderPojo.getChannelOrderId();
        try {
            byte[] invoiceBytes = invoiceFlowApi.generateInvoice(orderId);
            Files.write(Paths.get("invoice.pdf"), invoiceBytes);
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
        return orderPojo;
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
