package org.learning.assure.dto.helper;

import org.learning.assure.model.data.UserData;
import org.learning.assure.model.form.UserForm;


public class UserHelper {
    public static org.learning.assure.pojo.UserPojo convert(UserForm userForm) {
        org.learning.assure.pojo.UserPojo userPojo = new org.learning.assure.pojo.UserPojo();
        userPojo.setUserType(userForm.getUserType());
        userPojo.setName(userForm.getName());
        return userPojo;
    }
    
    public static UserData convertToUserData(org.learning.assure.pojo.UserPojo userPojo) {
        UserData userData = new UserData();
        userData.setUserId(userPojo.getUserId());
        userData.setUserType(userPojo.getUserType());
        userData.setName(userPojo.getName());
        return userData;
    }
}
