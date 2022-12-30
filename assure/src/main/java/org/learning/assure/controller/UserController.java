package org.learning.assure.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.learning.assure.api.UserApi;
import org.learning.assure.dto.UserDto;
import org.learning.assure.exception.ApiException;
import org.learning.assure.model.form.UserForm;
import org.learning.assure.pojo.UserPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api
@RestController
public class UserController {

    @Autowired
    private UserApi userApi;
    @Autowired
    private UserDto userDto;
    @PostMapping(path = "/user")
    @ApiOperation(value = "Create a user")
    public void addUser(@RequestBody UserForm userForm) throws ApiException {
        userDto.addUser(userForm);
    }

    @GetMapping(path = "/user/{id}")
    @ApiOperation(value = "Get User by User ID")
    public UserPojo getUserByUserId(@PathVariable Long id) {
        return userDto.getUserByUserId(id);
    }

    @GetMapping(path = "/user")
    @ApiOperation(value = "Get all Users")
    public List<UserPojo> getAllUsers() {
        return userDto.getAllUsers();
    }

    @DeleteMapping(path = "/user/{id}")
    @ApiOperation(value = "Delete user by UserU ID")
    public void deleteUserByUserId(@PathVariable Long id) {
        userDto.deleteUser(id);
    }

}
