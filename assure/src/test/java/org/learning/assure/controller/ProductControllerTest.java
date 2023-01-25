package org.learning.assure.controller;

import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;
import org.learning.assure.api.UserApi;
import org.learning.assure.config.AbstractUnitTest;
import org.learning.assure.exception.ApiException;
import org.learning.assure.pojo.ProductPojo;
import org.learning.assure.pojo.UserPojo;
import org.learning.assure.util.FileUtil;
import org.learning.assure.util.TestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ProductControllerTest extends AbstractUnitTest {
    private String csvDir = "data/product/";

    @Autowired
    private ProductController productController;

    @Autowired
    private UserApi userApi;

    @Test
    public void testAddProducts() throws IOException, ApiException {
        String csvFileName = "product_ok.csv";
        MultipartFile csvFile = null;
        String filePath = csvDir + csvFileName;
        csvFile = FileUtil.loadCSV(filePath, csvFileName);
        UserPojo client = TestUtil.createClient();
        userApi.addUser(client);
        List<ProductPojo> productPojoList = productController.addProducts(csvFile, client.getUserId());
        Assert.assertEquals(2, productPojoList.size());
        Assert.assertEquals(Optional.of("mock1").get(), productPojoList.get(0).getClientSkuId());
    }

    @Test
    public void addProductsWithMissingFieldsTest() throws IOException {
        String csvFileName = "product_missing.csv";
        MultipartFile csvFile = null;
        String filePath = csvDir + csvFileName;
        csvFile = FileUtil.loadCSV(filePath, csvFileName);
        UserPojo client = TestUtil.createClient();
        userApi.addUser(client);
        try {
            productController.addProducts(csvFile, client.getUserId());
            Assert.fail();
        } catch (ApiException e) {
            Assert.assertEquals("Error parsing CSV File :" +
                    " Field 'name' is mandatory but no value was provided at line number 2," +
                    " Field 'mrp' is mandatory but no value was provided at line number 2," +
                    " Field 'clientSkuId' is mandatory but no value was provided at line number 3," +
                    " Field 'brandId' is mandatory but no value was provided at line number 3," +
                    " Field 'description' is mandatory but no value was provided at line number 3"
                    ,e.getMessage());
        }
    }

    @Test
    public void addProductsWithValidationFails() {

    }
}