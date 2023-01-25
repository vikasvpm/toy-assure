package org.learning.assure.controller;

import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;
import org.learning.assure.api.ChannelApi;
import org.learning.assure.api.ProductApi;
import org.learning.assure.api.UserApi;
import org.learning.assure.config.AbstractUnitTest;
import org.learning.assure.exception.ApiException;
import org.learning.assure.pojo.ChannelListingPojo;
import org.learning.assure.pojo.ChannelPojo;
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
public class ChannelListingControllerTest extends AbstractUnitTest {

    @Autowired
    private ChannelListingController channelListingController;

    @Autowired
    private UserApi userApi;

    @Autowired
    private ProductApi productApi;

    @Autowired
    private ChannelApi channelApi;

    private String csvDir = "data/channelListing/";

    @Test
    public void testAddChannelListing() throws IOException, ApiException {
        String csvFileName = "chlisting_ok.csv";
        MultipartFile csvFile = null;
        String filePath = csvDir + csvFileName;
        csvFile = FileUtil.loadCSV(filePath, csvFileName);
        UserPojo client = userApi.addUser(TestUtil.createClient());
        productApi.addProducts(TestUtil.createProductList(client.getUserId()));
        ChannelPojo channelPojo = channelApi.addChannel(TestUtil.createChannel("Mock Channel"));
        List<ChannelListingPojo> channelListingPojoList = channelListingController.addChannelListing(csvFile, client.getUserId(),channelPojo.getChannelId());
        Assert.assertEquals(2, channelListingPojoList.size());
        Assert.assertEquals("cn1", channelListingPojoList.get(0).getChannelSkuId());
    }

    @Test
    public void testAddChannelListingWithInvalidClient() throws IOException {
        String csvFileName = "chlisting_ok.csv";
        MultipartFile csvFile = null;
        String filePath = csvDir + csvFileName;
        csvFile = FileUtil.loadCSV(filePath, csvFileName);
        UserPojo client = userApi.addUser(TestUtil.createClient());
        productApi.addProducts(TestUtil.createProductList(client.getUserId()));
        ChannelPojo channelPojo = channelApi.addChannel(TestUtil.createChannel("Mock Channel"));
        try {
            List<ChannelListingPojo> channelListingPojoList = channelListingController.addChannelListing(csvFile, 44L,channelPojo.getChannelId());
            Assert.fail();
        } catch (ApiException e) {
            Assert.assertEquals("No client exists with client Id = 44",e.getMessage());
        }
    }

    @Test
    public void testAddChannelListingWithInvalidChannel() throws IOException {
        String csvFileName = "chlisting_ok.csv";
        MultipartFile csvFile = null;
        String filePath = csvDir + csvFileName;
        csvFile = FileUtil.loadCSV(filePath, csvFileName);
        UserPojo client = userApi.addUser(TestUtil.createClient());
        productApi.addProducts(TestUtil.createProductList(client.getUserId()));
        ChannelPojo channelPojo = channelApi.addChannel(TestUtil.createChannel("Mock Channel"));
        try {
            List<ChannelListingPojo> channelListingPojoList = channelListingController.addChannelListing(csvFile, client.getUserId(),55L);
            Assert.fail();
        } catch (ApiException e) {
            Assert.assertEquals("Channel with channelId 55 does not exist",e.getMessage());
        }
    }

    @Test
    public void testAddChannelListingWithMissingFields() throws IOException {
        String csvFileName = "chlisting_missing.csv";
        MultipartFile csvFile = null;
        String filePath = csvDir + csvFileName;
        csvFile = FileUtil.loadCSV(filePath, csvFileName);
        UserPojo client = userApi.addUser(TestUtil.createClient());
        productApi.addProducts(TestUtil.createProductList(client.getUserId()));
        ChannelPojo channelPojo = channelApi.addChannel(TestUtil.createChannel("Mock Channel"));
        try {
            List<ChannelListingPojo> channelListingPojoList = channelListingController.addChannelListing(csvFile, client.getUserId(), channelPojo.getChannelId());
            Assert.fail();
        } catch (ApiException e) {
            Assert.assertEquals("Error parsing CSV File :" +
                    " Field 'clientSkuId' is mandatory but no value was provided at line number 2," +
                    " Field 'channelSkuId' is mandatory but no value was provided at line number 3"
                    ,e.getMessage());
        }
    }

    @Test
    public void testAddChannelListingWithValidationFails() throws IOException {
        String csvFileName = "chlisting_validation.csv";
        MultipartFile csvFile = null;
        String filePath = csvDir + csvFileName;
        csvFile = FileUtil.loadCSV(filePath, csvFileName);
        UserPojo client = userApi.addUser(TestUtil.createClient());
        productApi.addProducts(TestUtil.createProductList(client.getUserId()));
        ChannelPojo channelPojo = channelApi.addChannel(TestUtil.createChannel("Mock Channel"));
        try {
            List<ChannelListingPojo> channelListingPojoList = channelListingController.addChannelListing(csvFile, client.getUserId(), channelPojo.getChannelId());
            Assert.fail();
        } catch (ApiException e) {
            Assert.assertEquals("There is no product with Client SKU ID = mocsk1 for client 1," +
                            " There is no product with Client SKU ID = mocsk2 for client 1"
                    ,e.getMessage());
        }
    }
}