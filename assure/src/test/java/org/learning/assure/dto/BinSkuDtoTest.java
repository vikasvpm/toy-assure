package org.learning.assure.dto;

import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.learning.assure.api.BinSkuApi;
import org.learning.assure.config.AbstractUnitTest;
import org.learning.assure.exception.ApiException;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;

@RunWith(MockitoJUnitRunner.class)
public class BinSkuDtoTest extends AbstractUnitTest {

    @Autowired
    private BinSkuDto binSkuDto;

    @Mock
    private BinSkuApi binSkuApi;





}