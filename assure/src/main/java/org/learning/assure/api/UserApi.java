package org.learning.assure.api;

import org.learning.assure.dao.UserDao;
import org.learning.assure.exception.ApiException;
import org.learning.assure.pojo.UserPojo;
import org.learning.assure.model.enums.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class UserApi {
    
    @Autowired
    private UserDao userDao;

    @Transactional
    public UserPojo getUserByUserId(Long userId) {
        return userDao.getUserByUserId(userId);
    }

    @Transactional
    public List<UserPojo> getAllUsers() {
        return userDao.getAllUsers();
    }

    @Transactional
    public void addUser(UserPojo userPojo) {
        userDao.addUser(userPojo);
    }

    @Transactional
    public void deleteUser(Long userId) {
        userDao.deleteUserByUserId(userId);
    }

    public void invalidClientCheck(Long userId) throws ApiException {
        UserPojo userPojo = userDao.getUserByUserId(userId);
        if(userPojo == null) {
            throw new ApiException("No client exists with client Id = " + userId);
        }
        else if(!userPojo.getUserType().equals(UserType.CLIENT)){
            throw new ApiException("User with Id = " + userId + " is not a client");
        }
    }

    public void invalidCustomerCheck(Long customerId) throws ApiException {
        UserPojo userPojo = userDao.getUserByUserId(customerId);
        if(userPojo == null) {
            throw new ApiException("No customer exists with ID = " + customerId);
        }
        else if(!userPojo.getUserType().equals(UserType.CUSTOMER)){
            throw new ApiException("User with Id = " + customerId + " is not a customer");
        }
    }
}
