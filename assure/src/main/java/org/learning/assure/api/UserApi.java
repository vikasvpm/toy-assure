package org.learning.assure.api;

import org.learning.assure.dao.UserDao;
import org.learning.assure.exception.ApiException;
import org.learning.assure.pojo.UserPojo;
import org.learning.assure.model.enums.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class UserApi {
    
    @Autowired
    private UserDao userDao;

    @Transactional(readOnly = true)
    public UserPojo getUserByUserId(Long userId) {
        return userDao.getUserByUserId(userId);
    }

    @Transactional(readOnly = true)
    public UserPojo getUserByName(String name) {
        return userDao.getUserByName(name);
    }

    @Transactional(readOnly = true)
    public List<UserPojo> getAllUsers() {
        return userDao.getAllUsers();
    }

    public UserPojo addUser(UserPojo userPojo) {
        return userDao.addUser(userPojo);
    }



    public void invalidClientCheck(Long userId, List<String> errorList) throws ApiException {
        UserPojo userPojo = userDao.getUserByUserId(userId);
        if(Objects.isNull(userPojo)) {
            throw new ApiException("No client exists with client Id = " + userId);
        }
        else if(!userPojo.getUserType().equals(UserType.CLIENT)){
            throw new ApiException("User with Id = " + userId + " is not a client");
        }
    }

    public void invalidCustomerCheck(Long customerId) throws ApiException {
        UserPojo userPojo = userDao.getUserByUserId(customerId);
        if(Objects.isNull(userPojo)) {
            throw new ApiException("No customer exists with ID = " + customerId);
        }
        else if(!userPojo.getUserType().equals(UserType.CUSTOMER)){
            throw new ApiException("User with Id = " + customerId + " is not a customer");
        }
    }
}
