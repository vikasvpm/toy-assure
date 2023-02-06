package org.learning.assure.controller;

import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;
import org.learning.assure.config.AbstractUnitTest;
import org.learning.commons.exception.ApiException;
import org.learning.assure.pojo.BinPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class BinControllerTest extends AbstractUnitTest {

    @Autowired
    private BinController binController;

    @Test
    public void addZeroBinsTest() {
        try {
            binController.addBins(0l);
            Assert.fail();
        }
        catch (ApiException ex) {
            Assert.assertEquals("Number of bins to be created can not be 0 or negative", ex.getMessage());
        }
    }

    @Test
    public void addNegativeBinsTest() {
        try {
            binController.addBins(-3l);
            Assert.fail();
        }
        catch (ApiException ex) {
            Assert.assertEquals("Number of bins to be created can not be 0 or negative", ex.getMessage());
        }
    }

    @Test
    public void addedEqualsCreatedBins() throws ApiException {
        List<BinPojo> binPojoList = binController.addBins(5l);
        Assert.assertEquals(5, binPojoList.size());
    }
}