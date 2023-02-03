package org.learning.assure.controller;

import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;
import org.learning.assure.api.*;
import org.learning.assure.api.flow.AllocateOrderFlowApi;
import org.learning.assure.api.flow.BinWiseInventoryFlowApi;
import org.learning.assure.config.AbstractUnitTest;
import org.learning.commons.exception.ApiException;
import org.learning.assure.model.enums.OrderStatus;
import org.learning.assure.pojo.*;
import org.learning.assure.util.FileUtil;
import org.learning.assure.util.TestUtil;
import org.learning.commons.model.OrderForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
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

    @Autowired
    private AllocateOrderFlowApi allocateOrderFlowApi;

    @Test
    public void createChannelOrderTest() throws IOException, ApiException {
        UserPojo client = userApi.addUser(TestUtil.createClient());
        UserPojo customer = userApi.addUser(TestUtil.createCustomer());
        List<ProductPojo> createdProducts = productApi.addProducts(TestUtil.createProductList(client.getUserId()));
        ChannelPojo channelPojo = channelApi.addChannel(TestUtil.createChannel("Mock Channel"));
        List<ChannelListingPojo> channelListingPojoList = channelListingApi.addChannelListing(TestUtil.createChannelListingList(client.getUserId(), channelPojo.getChannelId(), createdProducts));
        OrderForm orderForm = TestUtil.createValidOrder(client.getUserId(),customer.getUserId(),channelPojo.getName());
        OrderPojo orderPojo = orderController.createChannelOrder(orderForm);
        Assert.assertNotNull(orderPojo);
        Assert.assertEquals("mock-ch-order-1", orderPojo.getChannelOrderId());
        Assert.assertEquals(2, orderApi.getOrderItemsByOrderId(orderPojo.getOrderId()).size());
        Assert.assertEquals(Optional.of(3L).get(), orderApi.getOrderItemsByOrderId((orderPojo.getOrderId())).get(0).getOrderedQuantity());
    }

    @Test
    public void createChannelOrderWithInvalidClientTest() throws IOException {
        UserPojo client = userApi.addUser(TestUtil.createClient());
        UserPojo customer = userApi.addUser(TestUtil.createCustomer());
        List<ProductPojo> createdProducts = productApi.addProducts(TestUtil.createProductList(client.getUserId()));
        ChannelPojo channelPojo = channelApi.addChannel(TestUtil.createChannel("Mock Channel"));
        List<ChannelListingPojo> channelListingPojoList = channelListingApi.addChannelListing(TestUtil.createChannelListingList(client.getUserId(), channelPojo.getChannelId(), createdProducts));
        try {
            OrderForm noClient = TestUtil.createValidOrder(4L,customer.getUserId(),channelPojo.getName());
            OrderPojo orderPojo = orderController.createChannelOrder(noClient);
            Assert.fail();
        }
        catch (ApiException ex) {
            Assert.assertEquals("No client exists with client Id = " + 4L, ex.getMessage());
        }
        try {
            OrderForm badClient = TestUtil.createValidOrder(customer.getUserId(), customer.getUserId(),channelPojo.getName());
            OrderPojo orderPojo = orderController.createChannelOrder(badClient);
            Assert.fail();
        }
        catch (ApiException ex) {
            Assert.assertEquals("User with Id = " + customer.getUserId() + " is not a client", ex.getMessage());
        }
    }

    @Test
    public void createChannelOrderWithInvalidCustomerTest() throws IOException {
        UserPojo client = userApi.addUser(TestUtil.createClient());
        UserPojo customer = userApi.addUser(TestUtil.createCustomer());
        List<ProductPojo> createdProducts = productApi.addProducts(TestUtil.createProductList(client.getUserId()));
        ChannelPojo channelPojo = channelApi.addChannel(TestUtil.createChannel("Mock Channel"));
        List<ChannelListingPojo> channelListingPojoList = channelListingApi.addChannelListing(TestUtil.createChannelListingList(client.getUserId(), channelPojo.getChannelId(), createdProducts));
        try {
            OrderForm noCustomer = TestUtil.createValidOrder(client.getUserId(),55L,channelPojo.getName());
            OrderPojo orderPojo = orderController.createChannelOrder(noCustomer);
            Assert.fail();
        }
        catch (ApiException ex) {
            Assert.assertEquals("No customer exists with ID = " + 55L, ex.getMessage());
        }
        try {
            OrderForm badCustomer = TestUtil.createValidOrder(client.getUserId(),client.getUserId(),channelPojo.getName());
            OrderPojo orderPojo = orderController.createChannelOrder(badCustomer);
            Assert.fail();
        }
        catch (ApiException ex) {
            Assert.assertEquals("User with Id = " + client.getUserId() + " is not a customer", ex.getMessage());
        }
    }

    @Test
    public void createChannelOrderWithInvalidChannelTest() throws IOException{
        UserPojo client = userApi.addUser(TestUtil.createClient());
        UserPojo customer = userApi.addUser(TestUtil.createCustomer());
        List<ProductPojo> createdProducts = productApi.addProducts(TestUtil.createProductList(client.getUserId()));
        ChannelPojo channelPojo = channelApi.addChannel(TestUtil.createChannel("Mock Channel"));
        List<ChannelListingPojo> channelListingPojoList = channelListingApi.addChannelListing(TestUtil.createChannelListingList(client.getUserId(), channelPojo.getChannelId(), createdProducts));
        OrderForm orderForm = TestUtil.createValidOrder(client.getUserId(),customer.getUserId(),"Not Channel");
        try {
            OrderPojo orderPojo = orderController.createChannelOrder(orderForm);
            Assert.fail();
        }
        catch (ApiException ex) {
            Assert.assertEquals("No Channel exists with Channel Name = Not Channel", ex.getMessage());
        }
    }

    @Test
    public void createChannelOrderWithMissingFieldsTest() throws IOException {
        UserPojo client = userApi.addUser(TestUtil.createClient());
        UserPojo customer = userApi.addUser(TestUtil.createCustomer());
        List<ProductPojo> createdProducts = productApi.addProducts(TestUtil.createProductList(client.getUserId()));
        ChannelPojo channelPojo = channelApi.addChannel(TestUtil.createChannel("Mock Channel"));
        List<ChannelListingPojo> channelListingPojoList = channelListingApi.addChannelListing(TestUtil.createChannelListingList(client.getUserId(), channelPojo.getChannelId(), createdProducts));
        OrderForm orderForm = TestUtil.createMissingItemsOrder(client.getUserId(),customer.getUserId(),channelPojo.getName());
        try {
            OrderPojo orderPojo = orderController.createChannelOrder(orderForm);
            Assert.fail();
        }
        catch (ApiException ex) {
            Assert.assertEquals(
                    "Channel SKU ID can not be null or blank, found null/blank value for order item = 1," +
                            " Selling price per unit can not be null, found null value for order item = 2",
                    ex.getMessage());
        }
    }

    @Test
    public void createChannelOrderWithValidationFailsTest() throws IOException {
        UserPojo client = userApi.addUser(TestUtil.createClient());
        UserPojo customer = userApi.addUser(TestUtil.createCustomer());
        List<ProductPojo> createdProducts = productApi.addProducts(TestUtil.createProductList(client.getUserId()));
        ChannelPojo channelPojo = channelApi.addChannel(TestUtil.createChannel("Mock Channel"));
        List<ChannelListingPojo> channelListingPojoList = channelListingApi.addChannelListing(TestUtil.createChannelListingList(client.getUserId(), channelPojo.getChannelId(), createdProducts));
        OrderForm orderForm = TestUtil.createInvalidOrder(client.getUserId(),customer.getUserId(),channelPojo.getName());
        try {
            OrderPojo orderPojo = orderController.createChannelOrder(orderForm);
            Assert.fail();
        }
        catch (ApiException ex) {
            Assert.assertEquals("No product with channelSkuId = cnx present for client 1 and channel 1," +
                    " No product with channelSkuId = cnk present for client 1 and channel 1"
                    , ex.getMessage());
        }
    }

    @Test
    public void createInternalOrderTest() throws IOException, ApiException {
        String csvFileName = "inorder_ok.csv";
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
    }

    @Test
    public void createInternalOrderWithInvalidClientTest() throws IOException {
        String csvFileName = "inorder_ok.csv";
        MultipartFile csvFile = null;
        String filePath = csvDir + csvFileName;
        csvFile = FileUtil.loadCSV(filePath, csvFileName);
        UserPojo client = userApi.addUser(TestUtil.createClient());
        UserPojo customer = userApi.addUser(TestUtil.createCustomer());
        List<ProductPojo> createdProducts = productApi.addProducts(TestUtil.createProductList(client.getUserId()));
        ChannelPojo channelPojo = channelApi.addChannel(TestUtil.createChannel("INTERNAL"));
        ChannelPojo internalChannel = channelApi.getChannelByName("INTERNAL");
        List<ChannelListingPojo> channelListingPojoList = channelListingApi.addChannelListing(TestUtil.createChannelListingList(client.getUserId(), internalChannel.getChannelId(), createdProducts));
        try {
            OrderPojo orderPojo = orderController.createInternalOrder(csvFile, 4L, "mock-internal-order", customer.getUserId());
            Assert.fail();
        }
        catch (ApiException ex) {
            Assert.assertEquals("No client exists with client Id = " + 4L, ex.getMessage());
        }
        try {
            OrderPojo orderPojo = orderController.createInternalOrder(csvFile, customer.getUserId(), "mock-internal-order", customer.getUserId());
            Assert.fail();
        }
        catch (ApiException ex) {
            Assert.assertEquals("User with Id = " + customer.getUserId() + " is not a client", ex.getMessage());
        }
    }

    @Test
    public void createInternalOrderWithInvalidCustomerTest() throws IOException {
        String csvFileName = "inorder_ok.csv";
        MultipartFile csvFile = null;
        String filePath = csvDir + csvFileName;
        csvFile = FileUtil.loadCSV(filePath, csvFileName);
        UserPojo client = userApi.addUser(TestUtil.createClient());
        UserPojo customer = userApi.addUser(TestUtil.createCustomer());
        List<ProductPojo> createdProducts = productApi.addProducts(TestUtil.createProductList(client.getUserId()));
        ChannelPojo channelPojo = channelApi.addChannel(TestUtil.createChannel("INTERNAL"));
        ChannelPojo internalChannel = channelApi.getChannelByName("INTERNAL");
        List<ChannelListingPojo> channelListingPojoList = channelListingApi.addChannelListing(TestUtil.createChannelListingList(client.getUserId(), internalChannel.getChannelId(), createdProducts));
        try {
            OrderPojo orderPojo = orderController.createInternalOrder(csvFile, client.getUserId(), "mock-internal-order", 55L);
            Assert.fail();
        }
        catch (ApiException ex) {
            Assert.assertEquals("No customer exists with ID = " + 55L, ex.getMessage());
        }
        try {
            OrderPojo orderPojo = orderController.createInternalOrder(csvFile, client.getUserId(), "mock-internal-order", client.getUserId());
            Assert.fail();
        }
        catch (ApiException ex) {
            Assert.assertEquals("User with Id = " + client.getUserId() + " is not a customer", ex.getMessage());
        }
    }

    @Test
    public void createInternalOrderWithNoInternalChannelTest() throws IOException {
        String csvFileName = "inorder_ok.csv";
        MultipartFile csvFile = null;
        String filePath = csvDir + csvFileName;
        csvFile = FileUtil.loadCSV(filePath, csvFileName);
        UserPojo client = userApi.addUser(TestUtil.createClient());
        UserPojo customer = userApi.addUser(TestUtil.createCustomer());
        List<ProductPojo> createdProducts = productApi.addProducts(TestUtil.createProductList(client.getUserId()));
        try {
            OrderPojo orderPojo = orderController.createInternalOrder(csvFile, client.getUserId(), "mock-internal-order", customer.getUserId());
            Assert.fail();
        }
        catch (ApiException ex) {

        }
    }

    @Test
    public void createInternalOrderWithMissingFields() throws IOException {
        String csvFileName = "inorder_missing.csv";
        MultipartFile csvFile = null;
        String filePath = csvDir + csvFileName;
        csvFile = FileUtil.loadCSV(filePath, csvFileName);
        UserPojo client = userApi.addUser(TestUtil.createClient());
        UserPojo customer = userApi.addUser(TestUtil.createCustomer());
        List<ProductPojo> createdProducts = productApi.addProducts(TestUtil.createProductList(client.getUserId()));
        ChannelPojo channelPojo = channelApi.addChannel(TestUtil.createChannel("INTERNAL"));
        ChannelPojo internalChannel = channelApi.getChannelByName("INTERNAL");
        List<ChannelListingPojo> channelListingPojoList = channelListingApi.addChannelListing(TestUtil.createChannelListingList(client.getUserId(), internalChannel.getChannelId(), createdProducts));
        try {
            OrderPojo orderPojo = orderController.createInternalOrder(csvFile, client.getUserId(), "mock-internal-order", customer.getUserId());
            Assert.fail();
        } catch (ApiException ex) {
            Assert.assertEquals("Field 'orderedQuantity' is mandatory but no value was provided at line number 2," +
                    " Field 'clientSkuId' is mandatory but no value was provided at line number 3", ex.getMessage());
        }
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

    @Test
    public void fulfillTest() throws ApiException {
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
        OrderPojo allocatedOrder = allocateOrderFlowApi.allocateOrder(orderPojo.getOrderId());
        MockHttpServletResponse response = new MockHttpServletResponse();
        OrderPojo fulfilledOrder = orderController.fulfillOrder(allocatedOrder.getOrderId(), response);
        Assert.assertEquals(OrderStatus.FULFILLED, fulfilledOrder.getOrderStatus());
    }

}