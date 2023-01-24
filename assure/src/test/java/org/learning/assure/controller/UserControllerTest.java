package org.learning.assure.controller;


import org.junit.Assert;
import org.junit.Test;
import org.learning.assure.config.AbstractUnitTest;
import org.learning.assure.exception.ApiException;
import org.learning.assure.model.enums.UserType;
import org.learning.assure.model.form.UserForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserControllerTest extends AbstractUnitTest {

    @Autowired
    private UserController userController;
    @Test
    public void addUserTest() throws ApiException {
        UserForm userForm = new UserForm();
        userForm.setUserType(UserType.CLIENT);
        userForm.setName("Dummy");
        org.learning.assure.pojo.UserPojo userPojo = userController.addUser(userForm);
        Assert.assertEquals("Dummy", userPojo.getName());
        Assert.assertEquals(UserType.CLIENT, userPojo.getUserType());

    }

    @Test
    public void duplicateUserTest() throws ApiException {
        UserForm userForm = new UserForm();
        userForm.setUserType(UserType.CLIENT);
        userForm.setName("Dummy");
        org.learning.assure.pojo.UserPojo userPojo = userController.addUser(userForm);
        try {
            userController.addUser(userForm);
            Assert.fail();
        }
        catch (ApiException ex) {
            Assert.assertEquals("User with name = " + userForm.getName() + " already exists in the system", ex.getMessage());
        }
    }

}
