package org.learning.assure.dto;

import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;
import org.learning.assure.config.AbstractUnitTest;
import org.learning.assure.exception.ApiException;
import org.learning.assure.pojo.BinPojo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class BinDtoTest extends AbstractUnitTest {

    @Autowired
    private BinDto binDto;

    @Test
    public void addInvalidNumberTest() {
        try {
            binDto.addBins(0l);
            Assert.fail();
        }
        catch (ApiException ex) {
            Assert.assertEquals("Number of bins to be created can not be 0 or negative", ex.getMessage());
        }
    }

    @Test
    public void addedEqualsCreatedBins() throws ApiException {
        List<BinPojo> binPojoList = binDto.addBins(5l);
        Assert.assertEquals(5, binPojoList.size());
    }

}