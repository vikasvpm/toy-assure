package org.learning.assure.dto.helper;

import org.learning.assure.model.data.UserData;
import org.learning.assure.model.form.UserForm;
import org.learning.assure.pojo.UserPojo;


public class UserHelper {
    public static UserPojo convert(UserForm userForm) {
        UserPojo userPojo = new UserPojo();
        userPojo.setUserType(userForm.getUserType());
        userPojo.setName(userForm.getName());
        return userPojo;
    }
    
    public static UserData convertToUserData(UserPojo userPojo) {
        UserData userData = new UserData();
        userData.setUserId(userPojo.getUserId());
        userData.setUserType(userPojo.getUserType());
        userData.setName(userPojo.getName());
        return userData;
    }
}
