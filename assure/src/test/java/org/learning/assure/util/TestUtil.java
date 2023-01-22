package org.learning.assure.util;

import org.learning.assure.model.enums.UserType;
import org.learning.assure.pojo.BinPojo;
import org.learning.assure.pojo.BinSkuPojo;
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

    public static List<ProductPojo> createProductList() {
        List<ProductPojo> productPojoList = new ArrayList<>();
        ProductPojo productPojo1 = new ProductPojo();
        productPojo1.setName("Mock Product 1");
        productPojo1.setMrp(100.00);
        productPojo1.setClientId(1L);
        productPojo1.setGlobalSkuId(1L);
        productPojo1.setDescription("mock");
        productPojo1.setBrandId("brand1");
        productPojo1.setClientSkuId("mock1");
        productPojoList.add(productPojo1);
        ProductPojo productPojo2 = new ProductPojo();
        productPojo2.setName("Mock Product 2");
        productPojo2.setMrp(100.00);
        productPojo2.setClientId(1L);
        productPojo2.setGlobalSkuId(2L);
        productPojo2.setDescription("mock");
        productPojo2.setBrandId("brand2");
        productPojo2.setClientSkuId("mock2");
        productPojoList.add(productPojo2);
        return productPojoList;
    }
}
