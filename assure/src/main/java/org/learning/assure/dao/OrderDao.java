package org.learning.assure.dao;

import org.learning.assure.pojo.OrderItemPojo;
import org.learning.assure.pojo.OrderPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class OrderDao {

    @PersistenceContext
    EntityManager entityManager;

    public OrderPojo createInternalOrder(OrderPojo orderPojo) {
       entityManager.persist(orderPojo);
       return orderPojo;
    }

    public void createInternalOrderItem(OrderItemPojo orderItemPojo) {
        entityManager.persist(orderItemPojo);
    }
}
