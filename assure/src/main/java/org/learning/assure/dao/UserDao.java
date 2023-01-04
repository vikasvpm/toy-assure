package org.learning.assure.dao;

import org.learning.assure.exception.ApiException;
import org.learning.assure.pojo.ProductPojo;
import org.learning.assure.pojo.UserPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class UserDao {

    @PersistenceContext
    EntityManager entityManager;

    public static final String SELECT_ALL = "select u from UserPojo u";
    public static final String SELECT_BY_USERID = "select u from UserPojo u where userID=:userId";

    private static final String DELETE_BY_USERID = "delete from UserPojo u where userId=:userId";

    public List<UserPojo> getAllUsers() {
        Query query = entityManager.createQuery(SELECT_ALL);
        return query.getResultList();
    }

    public UserPojo getUserByUserId(Long userId) {
        TypedQuery<UserPojo> query = entityManager.createQuery(SELECT_BY_USERID, UserPojo.class);
        query.setParameter("userId", userId);
        return query.getResultList().stream().findFirst().orElse(null);
    }

    public void addUser(UserPojo userPojo) {
        entityManager.persist(userPojo);
    }

    public int deleteUserByUserId(Long userId) {
        Query query = entityManager.createQuery(DELETE_BY_USERID);
        query.setParameter("userId", userId);
        return query.executeUpdate();
    }
}
