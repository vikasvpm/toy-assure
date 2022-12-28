package org.learning.assure.api;

import org.learning.assure.dao.UserDao;
import org.learning.assure.pojo.UserPojo;
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
    
}
