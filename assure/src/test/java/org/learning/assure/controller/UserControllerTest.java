package org.learning.assure.controller;


import org.junit.Assert;
import org.junit.Test;
import org.learning.assure.api.UserApi;
import org.learning.assure.config.AbstractUnitTest;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sound.midi.Soundbank;

public class UserControllerTest extends AbstractUnitTest{

    @Autowired
    private UserApi userApi;

    @Test
    public void rand() {
        Assert.assertNotEquals(1,2);
    }

    @Test
    public void getUser() {
        System.out.println(userApi.getUserByUserId(1l));
    }



}
