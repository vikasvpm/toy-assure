package org.learning.assure.dto;

import org.learning.assure.api.UserApi;
import org.learning.assure.dto.helper.UserHelper;
import org.learning.assure.exception.ApiException;
import org.learning.assure.model.form.UserForm;
import org.learning.assure.pojo.UserPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Objects;

@Service
public class UserDto {

    @Autowired
    private UserApi userApi;
    public org.learning.assure.pojo.UserPojo addUser(UserForm userForm) throws ApiException {
        validateForDuplicate(userForm);
        UserPojo userPojo = UserHelper.convert(userForm);
        userApi.addUser(userPojo);
        return userPojo;
    }

    private void validateForDuplicate(UserForm userForm) throws ApiException {
        UserPojo userPojo = userApi.getUserByName(userForm.getName());
        if(!Objects.isNull(userPojo)) {
            throw new ApiException("User with name = " + userForm.getName() + " already exists in the system");
        }
    }

    public UserPojo getUserByUserId(Long id) {
        return userApi.getUserByUserId(id);
    }

    public List<UserPojo> getAllUsers() {
        return userApi.getAllUsers();
    }

}
