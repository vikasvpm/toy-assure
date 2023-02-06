package org.learning.assure.dto;

import org.learning.assure.api.UserApi;
import org.learning.assure.dto.helper.ThrowExceptionHelper;
import org.learning.assure.dto.helper.UserHelper;
import org.learning.commons.exception.ApiException;
import org.learning.assure.model.form.UserForm;
import org.learning.assure.pojo.UserPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class UserDto {

    @Autowired
    private UserApi userApi;
    public UserPojo addUser(UserForm userForm) throws ApiException {
        validateNullValues(userForm);
        validateForDuplicate(userForm);
        UserPojo userPojo = UserHelper.convert(userForm);
        userApi.addUser(userPojo);
        return userPojo;
    }

    private void validateNullValues(UserForm userForm) throws ApiException {
        List<String> errors = new ArrayList<>();
        if(Objects.isNull(userForm.getName()) || userForm.getName().equals("")) {
            errors.add("User Name must be provided");
        }
        if(Objects.isNull(userForm.getUserType())) {
            errors.add("User Type must be provided");
        }
        ThrowExceptionHelper.throwIfErrors(errors);
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
