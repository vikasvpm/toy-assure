package org.learning.assure.dto;

import org.learning.assure.api.UserApi;
import org.learning.assure.dto.helper.UserHelper;
import org.learning.assure.model.data.UserData;
import org.learning.assure.model.form.UserForm;
import org.learning.assure.pojo.UserPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class UserDto {

    @Autowired
    private UserApi userApi;
    public void addUser(UserForm userForm) {
        UserPojo userPojo = UserHelper.convert(userForm);
        userApi.addUser(userPojo);
    }

    public UserPojo getUserByUserId(Long id) {
        return userApi.getUserByUserId(id);
    }

    public List<UserPojo> getAllUsers() {
        return userApi.getAllUsers();
    }

    public void deleteUser(@PathVariable Long id) {
        userApi.deleteUser(id);
    }
}
