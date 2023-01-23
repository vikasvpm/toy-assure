package org.learning.assure.util;

import org.learning.assure.model.enums.InvoiceType;
import org.learning.assure.model.enums.OrderStatus;
import org.learning.assure.model.enums.UserType;
import org.learning.assure.model.form.ChannelForm;
import org.learning.assure.pojo.*;
import java.util.ArrayList;
import java.util.List;

public class TestUtil {
    public static UserPojo createClient() {
        UserPojo userPojo = new UserPojo();
        userPojo.setUserType(UserType.CLIENT);
        userPojo.setName("MockClient");
        return userPojo;
    }
    public static UserPojo createCustomer() {
        UserPojo userPojo = new UserPojo();
        userPojo.setUserType(UserType.CUSTOMER);
        userPojo.setName("MockCustomer");
        return userPojo;
    }

    public static BinPojo createBin() {
        BinPojo binPojo = new BinPojo();
        return binPojo;
    }

    public static List<ProductPojo> createProductList(Long clientId) {
        List<ProductPojo> productPojoList = new ArrayList<>();
        ProductPojo productPojo1 = new ProductPojo();
        productPojo1.setName("Mock Product 1");
        productPojo1.setMrp(100.00);
        productPojo1.setClientId(clientId);
        productPojo1.setGlobalSkuId(1L);
        productPojo1.setDescription("mock");
        productPojo1.setBrandId("brand1");
        productPojo1.setClientSkuId("mock1");
        productPojoList.add(productPojo1);
        ProductPojo productPojo2 = new ProductPojo();
        productPojo2.setName("Mock Product 2");
        productPojo2.setMrp(100.00);
        productPojo2.setClientId(clientId);
        productPojo2.setGlobalSkuId(2L);
        productPojo2.setDescription("mock");
        productPojo2.setBrandId("brand2");
        productPojo2.setClientSkuId("mock2");
        productPojoList.add(productPojo2);
        return productPojoList;
    }

    public static ChannelForm createChannelForm(String name) {
        ChannelForm channelForm = new ChannelForm();
        channelForm.setName(name);
        channelForm.setInvoiceType(InvoiceType.CHANNEL);
        return channelForm;
    }

    public static ChannelPojo createChannel(String name) {
        ChannelPojo channelPojo = new ChannelPojo();
        channelPojo.setChannelId(1L);
        channelPojo.setName(name);
        channelPojo.setInvoiceType(InvoiceType.CHANNEL);
        return channelPojo;
    }
    public static List<ChannelListingPojo> createChannelListingList(Long userId, Long channelId, List<ProductPojo> productPojoList) {
        List<ChannelListingPojo> created = new ArrayList<>();
        ChannelListingPojo pojo1 = new ChannelListingPojo();
        pojo1.setChannelId(channelId);
        pojo1.setChannelSkuId("cn1");
        pojo1.setClientId(userId);
        pojo1.setGlobalSkuId(productPojoList.get(0).getGlobalSkuId());
        created.add(pojo1);
        ChannelListingPojo pojo2 = new ChannelListingPojo();
        pojo2.setChannelId(channelId);
        pojo2.setChannelSkuId("cn2");
        pojo2.setClientId(userId);
        pojo2.setGlobalSkuId(productPojoList.get(1).getGlobalSkuId());
        created.add(pojo2);
        return created;
    }

    public static List<BinSkuPojo> createBinSkus(List<ProductPojo> productPojoList, Long quantity) {
        List<BinSkuPojo> created = new ArrayList<>();
        BinSkuPojo pojo1 = new BinSkuPojo();
        pojo1.setBinId(1L);
        pojo1.setGlobalSkuId(productPojoList.get(0).getGlobalSkuId());
        pojo1.setQuantity(quantity);
        created.add(pojo1);
        BinSkuPojo pojo2 = new BinSkuPojo();
        pojo2.setBinId(2L);
        pojo2.setGlobalSkuId(productPojoList.get(0).getGlobalSkuId());
        pojo2.setQuantity(quantity);
        created.add(pojo2);
        return created;
    }

    public static OrderPojo createOrder(Long clientId, Long customerId, Long channelId, String channelOrderId, OrderStatus status) {
        OrderPojo orderPojo = new OrderPojo();
        orderPojo.setClientId(clientId);
        orderPojo.setCustomerId(customerId);
        orderPojo.setChannelId(channelId);
        orderPojo.setChannelOrderId(channelOrderId);
        orderPojo.setOrderStatus(status);
        return orderPojo;
    }

    public static List<OrderItemPojo> createOrderItems(List<ProductPojo> createdProducts) {
        List<OrderItemPojo> orderItemPojoList = new ArrayList<>();
        for(ProductPojo productPojo : createdProducts) {
            OrderItemPojo orderItemPojo = new OrderItemPojo();
            orderItemPojo.setGlobalSkuId(productPojo.getGlobalSkuId());
            orderItemPojo.setOrderedQuantity(5L);
            orderItemPojo.setSellingPricePerUnit(30.00);
            orderItemPojo.setFulfilledQuantity(0L);
            orderItemPojo.setAllocatedQuantity(0L);
            orderItemPojoList.add(orderItemPojo);
        }
        return orderItemPojoList;
    }

    public static List<InventoryPojo> createInventoryPojos(List<ProductPojo> createdProducts, Long quantity) {
        List<InventoryPojo> inventoryPojoList = new ArrayList<>();
        for(ProductPojo productPojo : createdProducts) {
            InventoryPojo inventoryPojo = new InventoryPojo();
            inventoryPojo.setAvailableQuantity(quantity);
            inventoryPojo.setAllocatedQuantity(0L);
            inventoryPojo.setFulfilledQuantity(0L);
            inventoryPojo.setGlobalSkuId(productPojo.getGlobalSkuId());
            inventoryPojoList.add(inventoryPojo);
        }
        return inventoryPojoList;
    }
}
