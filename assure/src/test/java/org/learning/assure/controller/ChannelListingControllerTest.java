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
        String csvFileName = "validChannelListingCsv.csv";
        MultipartFile csvFile = null;
        String filePath = csvDir + csvFileName;
        csvFile = FileUtil.loadCSV(filePath, csvFileName);
        UserPojo client = userApi.addUser(TestUtil.createClient());
        productApi.addProducts(TestUtil.createProductList(client.getUserId()));
        ChannelPojo channelPojo = channelApi.addChannel(TestUtil.createChannel("Mock Channel"));
        List<ChannelListingPojo> channelListingPojoList = channelListingController.addChannelListing(csvFile, client.getUserId(),channelPojo.getChannelId());
        Assert.assertEquals(2, channelListingPojoList.size());
    }
}