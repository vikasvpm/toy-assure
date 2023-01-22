package org.learning.assure.dto;

import org.junit.Assert;
import org.junit.Test;
import org.learning.assure.config.AbstractUnitTest;
import org.learning.assure.pojo.UserPojo;
import org.springframework.beans.factory.annotation.Autowired;
public class UserDtoTest extends AbstractUnitTest {

    @Autowired
    UserDto userDto;

    @Test
    public void testGetUserById() {
        UserPojo user = userDto.getUserByUserId(1l);
        Assert.assertEquals("MOCK_USER", user.getName());
    }
}