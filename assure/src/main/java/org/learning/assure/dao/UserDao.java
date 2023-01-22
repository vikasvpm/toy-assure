package org.learning.assure.dao;

import org.learning.assure.exception.ApiException;
import org.learning.assure.pojo.ProductPojo;
import org.learning.assure.pojo.UserPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
@Transactional
public class UserDao {

    @PersistenceContext
    EntityManager entityManager;

    public static final String SELECT_ALL = "select u from UserPojo u";
    public static final String SELECT_BY_USERID = "select u from UserPojo u where userID=:userId";

    public static final String SELECT_BY_NAME = "select u from UserPojo u where name=:name";
    private static final String DELETE_BY_USERID = "delete from UserPojo u where userId=:userId";



    @Transactional(readOnly = true)
    public List<UserPojo> getAllUsers() {
        Query query = entityManager.createQuery(SELECT_ALL);
        return query.getResultList();
    }
    @Transactional(readOnly = true)
    public UserPojo getUserByUserId(Long userId) {
        TypedQuery<UserPojo> query = entityManager.createQuery(SELECT_BY_USERID, UserPojo.class);
        query.setParameter("userId", userId);
        return query.getResultList().stream().findFirst().orElse(null);
    }

    public UserPojo addUser(UserPojo userPojo) {
        entityManager.persist(userPojo);
        return userPojo;
    }

    public int deleteUserByUserId(Long userId) {
        Query query = entityManager.createQuery(DELETE_BY_USERID);
        query.setParameter("userId", userId);
        return query.executeUpdate();
    }

    public UserPojo getUserByName(String name) {
        TypedQuery<UserPojo> query = entityManager.createQuery(SELECT_BY_NAME, UserPojo.class);
        query.setParameter("name", name);
        return query.getResultList().stream().findFirst().orElse(null);
    }
}
