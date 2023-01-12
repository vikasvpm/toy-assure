package org.learning.assure.dao;

import org.learning.assure.pojo.OrderItemPojo;
import org.learning.assure.pojo.OrderPojo;
import org.learning.assure.pojo.ProductPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

@Repository
public class OrderDao {

    @PersistenceContext
    EntityManager entityManager;
    public static final String SELECT_BY_CHANNELID_CHANNELORDERID = "select o from OrderPojo o where channelId=:channelId and channelOrderId=:channelOrderId";


    public OrderPojo createInternalOrder(OrderPojo orderPojo) {
       entityManager.persist(orderPojo);
       return orderPojo;
    }

    public void createInternalOrderItem(OrderItemPojo orderItemPojo) {
        entityManager.persist(orderItemPojo);
    }

    public OrderPojo getOrderByChannelOrder(String channelOrderId, Long channelId) {
        TypedQuery<OrderPojo> query = entityManager.createQuery(SELECT_BY_CHANNELID_CHANNELORDERID, OrderPojo.class);
        query.setParameter("channelId", channelId);
        query.setParameter("channelOrderId", channelOrderId);
        return query.getResultList().stream().findFirst().orElse(null);
    }
}
