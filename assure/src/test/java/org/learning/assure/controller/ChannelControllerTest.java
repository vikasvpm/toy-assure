package org.learning.assure.controller;

import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;
import org.learning.assure.config.AbstractUnitTest;
import org.learning.assure.exception.ApiException;
import org.learning.assure.model.form.ChannelForm;
import org.learning.assure.pojo.ChannelPojo;
import org.learning.assure.util.TestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Optional;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ChannelControllerTest extends AbstractUnitTest {

    @Autowired
    private ChannelController channelController;

    @Test
    public void testAddChannel() throws ApiException {
        ChannelForm channelForm = TestUtil.createChannelForm("Mock Channel");
        ChannelPojo channelPojo = channelController.addChannel(channelForm);
        Assert.assertEquals(Optional.of("Mock Channel").get(), channelPojo.getName());
    }
}