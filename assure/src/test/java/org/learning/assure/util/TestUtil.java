package org.learning.assure.util;

import org.learning.assure.model.enums.InvoiceType;
import org.learning.assure.model.enums.UserType;
import org.learning.assure.model.form.ChannelForm;
import org.learning.assure.pojo.BinPojo;
import org.learning.assure.pojo.ChannelPojo;
import org.learning.assure.pojo.ProductPojo;
import org.learning.assure.pojo.UserPojo;

import java.util.ArrayList;
import java.util.List;

public class TestUtil {
    public static UserPojo createClient() {
        UserPojo userPojo = new UserPojo();
        userPojo.setUserId(1l);
        userPojo.setUserType(UserType.CLIENT);
        userPojo.setName("MockClient");
        return userPojo;
    }
    public static UserPojo createCustomer() {
        UserPojo userPojo = new UserPojo();
        userPojo.setUserId(2l);
        userPojo.setUserType(UserType.CUSTOMER);
        userPojo.setName("MockCustomer");
        return userPojo;
    }

    public static BinPojo createBin(long l) {
        BinPojo binPojo = new BinPojo();
        binPojo.setBinId(l);
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
}
