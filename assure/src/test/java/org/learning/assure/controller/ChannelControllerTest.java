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

import java.util.Optional;

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