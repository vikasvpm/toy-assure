package org.learning.assure.dto;

import org.learning.assure.api.UserApi;
import org.learning.assure.dto.helper.UserHelper;
import org.learning.assure.exception.ApiException;
import org.learning.assure.model.form.UserForm;
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
        org.learning.assure.pojo.UserPojo userPojo = UserHelper.convert(userForm);
        userApi.addUser(userPojo);
        return userPojo;
    }

    private void validateForDuplicate(UserForm userForm) throws ApiException {
        org.learning.assure.pojo.UserPojo userPojo = userApi.getUserByName(userForm.getName());
        if(!Objects.isNull(userPojo)) {
            throw new ApiException("User with name = " + userForm.getName() + " already exists in the system");
        }
    }

    public org.learning.assure.pojo.UserPojo getUserByUserId(Long id) {
        return userApi.getUserByUserId(id);
    }

    public List<org.learning.assure.pojo.UserPojo> getAllUsers() {
        return userApi.getAllUsers();
    }

    public void deleteUser(@PathVariable Long id) {
        userApi.deleteUser(id);
    }
}
