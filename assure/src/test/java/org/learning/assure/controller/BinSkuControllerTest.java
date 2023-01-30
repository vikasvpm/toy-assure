package org.learning.assure.controller;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.learning.assure.api.BinApi;
import org.learning.assure.api.InventoryApi;
import org.learning.assure.api.ProductApi;
import org.learning.assure.api.UserApi;
import org.learning.assure.config.AbstractUnitTest;
import org.learning.assure.exception.ApiException;
import org.learning.assure.pojo.BinPojo;
import org.learning.assure.pojo.BinSkuPojo;
import org.learning.assure.pojo.UserPojo;
import org.learning.assure.util.FileUtil;
import org.learning.assure.util.TestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class BinSkuControllerTest extends AbstractUnitTest {

    private String csvDir = "data/bin/";

    @Autowired
    private BinSkuController binSkuController;

    @Autowired
    private UserApi userApi;

    @Autowired
    private BinApi binApi;

    @Autowired
    private ProductApi productApi;

    @Autowired
    private InventoryApi inventoryApi;

    @Test
    public void testAddBinSku() throws IOException, ApiException {
        String csvFileName = "bin_ok.csv";
        MultipartFile csvFile = null;
        String filePath = csvDir + csvFileName;
        csvFile = FileUtil.loadCSV(filePath, csvFileName);
        UserPojo client = userApi.addUser(TestUtil.createClient());
        BinPojo bin1 = binApi.addBin(TestUtil.createBin());
        BinPojo bin2 = binApi.addBin(TestUtil.createBin());
        productApi.addProducts(TestUtil.createProductList(client.getUserId()));
        List<BinSkuPojo> binSkuPojoList = binSkuController.addBinSkus(csvFile, client.getUserId());
        Assert.assertEquals(2, binSkuPojoList.size());
        Assert.assertEquals(Optional.of(5L).get(), binSkuPojoList.get(0).getQuantity());
        Assert.assertEquals(Optional.of(10L).get(), binSkuPojoList.get(1).getQuantity());
        Assert.assertEquals(Optional.of(5L).get(), inventoryApi.getByGlobalSkuId(1L).getAvailableQuantity());
        Assert.assertEquals(Optional.of(10L).get(), inventoryApi.getByGlobalSkuId(2L).getAvailableQuantity());
    }
    @Test
    public void testAddBinSkuWithoutClient() throws IOException {
        String csvFileName = "bin_ok.csv";
        MultipartFile csvFile = null;
        String filePath = csvDir + csvFileName;
        csvFile = FileUtil.loadCSV(filePath, csvFileName);
        try {
            binSkuController.addBinSkus(csvFile, 1l);
            Assert.fail();
        }
        catch (ApiException ex) {
            Assert.assertEquals("No client exists with client Id = 1", ex.getMessage());
        }
    }
    @Test
    public void testAddBinSkuWithoutBin() throws IOException, ApiException {
        String csvFileName = "bin_nobin.csv";
        MultipartFile csvFile = null;
        String filePath = csvDir + csvFileName;
        csvFile = FileUtil.loadCSV(filePath, csvFileName);
        UserPojo client = userApi.addUser(TestUtil.createClient());
        productApi.addProducts(TestUtil.createProductList(client.getUserId()));
        try {
            binSkuController.addBinSkus(csvFile, client.getUserId());
            Assert.fail();
        }
        catch (ApiException ex) {
            Assert.assertEquals("Bin with id " + 1L + " does not exist", ex.getMessage());
        }
    }

    @Test
    public void testAddBinSkuWithMissingFields() throws IOException {
        String csvFileName = "bin_missingfields.csv";
        MultipartFile csvFile = null;
        String filePath = csvDir + csvFileName;
        csvFile = FileUtil.loadCSV(filePath, csvFileName);
        UserPojo client = userApi.addUser(TestUtil.createClient());
        try {
            List<BinSkuPojo> binSkuPojoList = binSkuController.addBinSkus(csvFile, client.getUserId());
            Assert.fail();
        }
        catch (ApiException ex) {
            Assert.assertEquals(
                            "Field 'binId' is mandatory but no value was provided at line number 2," +
                            " Field 'clientSkuId' is mandatory but no value was provided at line number 3",
                    ex.getMessage()
            );
        }
    }

    @Test
    public void testAddBinSkuWithMultipleValidationFails() throws IOException {
        String csvFileName = "bin_validations.csv";
        MultipartFile csvFile = null;
        String filePath = csvDir + csvFileName;
        csvFile = FileUtil.loadCSV(filePath, csvFileName);
        UserPojo client = userApi.addUser(TestUtil.createClient());
        BinPojo bin1 = binApi.addBin(TestUtil.createBin());
        BinPojo bin2 = binApi.addBin(TestUtil.createBin());
        productApi.addProducts(TestUtil.createProductList(client.getUserId()));
        try {
            List<BinSkuPojo> binSkuPojoList = binSkuController.addBinSkus(csvFile, client.getUserId());
            Assert.fail();
        } catch (ApiException ex) {
            Assert.assertEquals("Product with Client SKU ID mockx1 does not exist for Client 1 in the system," +
                    " Bin with id 22 does not exist, " +
                    "Quantity of item can not be 0 or negative: Found such value for Product with client SKU ID = mock2",
                    ex.getMessage());
        }
    }
}