package org.learning.assure.controller;

import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;
import org.learning.assure.api.*;
import org.learning.assure.api.flow.BinWiseInventoryFlowApi;
import org.learning.assure.config.AbstractUnitTest;
import org.learning.assure.exception.ApiException;
import org.learning.assure.model.enums.OrderStatus;
import org.learning.assure.pojo.*;
import org.learning.assure.util.FileUtil;
import org.learning.assure.util.TestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class OrderControllerTest extends AbstractUnitTest {

    private String csvDir = "data/order/";

    @Autowired
    private OrderController orderController;

    @Autowired
    private UserApi userApi;

    @Autowired
    private BinApi binApi;

    @Autowired
    private OrderApi orderApi;

    @Autowired
    private ProductApi productApi;

    @Autowired
    private ChannelApi channelApi;

    @Autowired
    private ChannelListingApi channelListingApi;

    @Autowired
    private BinWiseInventoryFlowApi binWiseInventoryFlowApi;

    @Test
    public void createChannelOrderTest() throws IOException, ApiException {
        String csvFileName = "channelOrderValidCsv.csv";
        MultipartFile csvFile = null;
        String filePath = csvDir + csvFileName;
        csvFile = FileUtil.loadCSV(filePath, csvFileName);
        UserPojo client = userApi.addUser(TestUtil.createClient());
        UserPojo customer = userApi.addUser(TestUtil.createCustomer());
        List<ProductPojo> createdProducts = productApi.addProducts(TestUtil.createProductList(client.getUserId()));
        ChannelPojo channelPojo = channelApi.addChannel(TestUtil.createChannel("Mock Channel"));
        List<ChannelListingPojo> channelListingPojoList = channelListingApi.addChannelListing(TestUtil.createChannelListingList(client.getUserId(), channelPojo.getChannelId(), createdProducts));
        OrderPojo orderPojo = orderController.createChannelOrder(csvFile, client.getUserId(), "mock-channel-order", customer.getUserId(), channelPojo.getName());
        Assert.assertNotNull(orderPojo);
        Assert.assertEquals("mock-channel-order", orderPojo.getChannelOrderId());
        Assert.assertEquals(2, orderApi.getOrderItemsByOrderId(orderPojo.getOrderId()).size());
        Assert.assertEquals(Optional.of(3L).get(), orderApi.getOrderItemsByOrderId((orderPojo.getOrderId())).get(0).getOrderedQuantity());
    }

    @Test
    public void createChannelOrderWithInvalidClientTest() throws IOException {
        String csvFileName = "channelOrderValidCsv.csv";
        MultipartFile csvFile = null;
        String filePath = csvDir + csvFileName;
        csvFile = FileUtil.loadCSV(filePath, csvFileName);
        UserPojo client = userApi.addUser(TestUtil.createClient());
        UserPojo customer = userApi.addUser(TestUtil.createCustomer());
        List<ProductPojo> createdProducts = productApi.addProducts(TestUtil.createProductList(client.getUserId()));
//        binApi.addBin(new BinPojo());
//        binApi.addBin(new BinPojo());
//        binSkuApi.addBinSkus(TestUtil.createBinSkus(createdProducts));
        ChannelPojo channelPojo = channelApi.addChannel(TestUtil.createChannel("Mock Channel"));
        List<ChannelListingPojo> channelListingPojoList = channelListingApi.addChannelListing(TestUtil.createChannelListingList(client.getUserId(), channelPojo.getChannelId(), createdProducts));
        try {
            OrderPojo orderPojo = orderController.createChannelOrder(csvFile, 4L, "mock-channel-order", customer.getUserId(), channelPojo.getName());
            Assert.fail();
        }
        catch (ApiException ex) {
            Assert.assertEquals("No client exists with client Id = " + 4L, ex.getMessage());
        }
        try {
            OrderPojo orderPojo = orderController.createChannelOrder(csvFile, customer.getUserId(), "mock-channel-order", customer.getUserId(), channelPojo.getName());
            Assert.fail();
        }
        catch (ApiException ex) {
            Assert.assertEquals("User with Id = " + customer.getUserId() + " is not a client", ex.getMessage());
        }

    }
    @Test
    public void createInternalOrderTest() throws IOException, ApiException {
        String csvFileName = "internalOrderValidCsv.csv";
        MultipartFile csvFile = null;
        String filePath = csvDir + csvFileName;
        csvFile = FileUtil.loadCSV(filePath, csvFileName);
        UserPojo client = userApi.addUser(TestUtil.createClient());
        UserPojo customer = userApi.addUser(TestUtil.createCustomer());
        List<ProductPojo> createdProducts = productApi.addProducts(TestUtil.createProductList(client.getUserId()));
        ChannelPojo channelPojo = channelApi.addChannel(TestUtil.createChannel("INTERNAL"));
        ChannelPojo internalChannel = channelApi.getChannelByName("INTERNAL");
        List<ChannelListingPojo> channelListingPojoList = channelListingApi.addChannelListing(TestUtil.createChannelListingList(client.getUserId(), internalChannel.getChannelId(), createdProducts));
        OrderPojo orderPojo = orderController.createInternalOrder(csvFile, client.getUserId(), "mock-internal-order", customer.getUserId());
        Assert.assertNotNull(orderPojo);
        Assert.assertEquals("mock-internal-order", orderPojo.getChannelOrderId());
        Assert.assertEquals(2, orderApi.getOrderItemsByOrderId(orderPojo.getOrderId()).size());
        Assert.assertEquals(Optional.of(10L).get(), orderApi.getOrderItemsByOrderId((orderPojo.getOrderId())).get(0).getOrderedQuantity());
    }

    @Test
    public void totalAllocateTest() throws ApiException {
        UserPojo client = userApi.addUser(TestUtil.createClient());
        UserPojo customer = userApi.addUser(TestUtil.createCustomer());
        List<ProductPojo> createdProducts = productApi.addProducts(TestUtil.createProductList(client.getUserId()));
        binApi.addBin(new BinPojo());
        binApi.addBin(new BinPojo());
        binWiseInventoryFlowApi.addBinWiseInventory(TestUtil.createBinSkus(createdProducts, 5L), TestUtil.createInventoryPojos(createdProducts, 5L));
        ChannelPojo channelPojo = channelApi.addChannel(TestUtil.createChannel("INTERNAL"));
        ChannelPojo internalChannel = channelApi.getChannelByName("INTERNAL");
        List<ChannelListingPojo> channelListingPojoList = channelListingApi.addChannelListing(TestUtil.createChannelListingList(client.getUserId(), internalChannel.getChannelId(), createdProducts));
        OrderStatus status = OrderStatus.CREATED;
        OrderPojo orderPojo = orderApi.createOrderAndOrderItems(TestUtil.createOrder(client.getUserId(),customer.getUserId(),internalChannel.getChannelId(),"mock-internal-order", status),
                TestUtil.createOrderItems(createdProducts));
        List<OrderItemPojo> orderItemPojoList = orderApi.getOrderItemsByOrderId(orderPojo.getOrderId());
        OrderPojo allocatedOrder = orderController.allocateOrder(orderPojo.getOrderId());
        Assert.assertEquals(OrderStatus.ALLOCATED, allocatedOrder.getOrderStatus());
        Assert.assertEquals(orderApi.getOrderItemsByOrderId(allocatedOrder.getOrderId()).get(0).getAllocatedQuantity() ,orderApi.getOrderItemsByOrderId(allocatedOrder.getOrderId()).get(0).getOrderedQuantity());
    }

    @Test
    public void partialAllocateTest() throws ApiException {
        UserPojo client = userApi.addUser(TestUtil.createClient());
        UserPojo customer = userApi.addUser(TestUtil.createCustomer());
        List<ProductPojo> createdProducts = productApi.addProducts(TestUtil.createProductList(client.getUserId()));
        binApi.addBin(new BinPojo());
        binApi.addBin(new BinPojo());
        binWiseInventoryFlowApi.addBinWiseInventory(TestUtil.createBinSkus(createdProducts, 3L), TestUtil.createInventoryPojos(createdProducts, 3L));
        ChannelPojo channelPojo = channelApi.addChannel(TestUtil.createChannel("INTERNAL"));
        ChannelPojo internalChannel = channelApi.getChannelByName("INTERNAL");
        List<ChannelListingPojo> channelListingPojoList = channelListingApi.addChannelListing(TestUtil.createChannelListingList(client.getUserId(), internalChannel.getChannelId(), createdProducts));
        OrderStatus status = OrderStatus.CREATED;
        OrderPojo orderPojo = orderApi.createOrderAndOrderItems(TestUtil.createOrder(client.getUserId(),customer.getUserId(),internalChannel.getChannelId(),"mock-internal-order", status),
                TestUtil.createOrderItems(createdProducts));
        List<OrderItemPojo> orderItemPojoList = orderApi.getOrderItemsByOrderId(orderPojo.getOrderId());
        OrderPojo allocatedOrder = orderController.allocateOrder(orderPojo.getOrderId());
        Assert.assertNotEquals(OrderStatus.ALLOCATED, allocatedOrder.getOrderStatus());
        Assert.assertNotEquals(orderApi.getOrderItemsByOrderId(allocatedOrder.getOrderId()).get(0).getAllocatedQuantity() ,orderApi.getOrderItemsByOrderId(allocatedOrder.getOrderId()).get(0).getOrderedQuantity());
    }


}