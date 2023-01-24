package org.learning.assure.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.learning.assure.api.UserApi;
import org.learning.assure.dto.UserDto;
import org.learning.assure.exception.ApiException;
import org.learning.assure.model.form.UserForm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserApi userApi;
    @Autowired
    private UserDto userDto;
    @PostMapping(path = "")
    @ApiOperation(value = "Create a user")
    public org.learning.assure.pojo.UserPojo addUser(@Valid @RequestBody UserForm userForm) throws ApiException {
        return userDto.addUser(userForm);
    }

    @GetMapping(path = "/{id}")
    @ApiOperation(value = "Get User by User ID")
    public org.learning.assure.pojo.UserPojo getUserByUserId(@PathVariable Long id) {
        return userDto.getUserByUserId(id);
    }

    @GetMapping(path = "")
    @ApiOperation(value = "Get all Users")
    public List<org.learning.assure.pojo.UserPojo> getAllUsers() {
        return userDto.getAllUsers();
    }


}
