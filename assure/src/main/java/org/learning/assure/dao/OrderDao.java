package org.learning.assure.dao;

import org.learning.assure.model.enums.OrderStatus;
import org.learning.assure.pojo.OrderItemPojo;
import org.learning.assure.pojo.OrderPojo;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Transactional
@Repository
public class OrderDao {

    @PersistenceContext
    EntityManager entityManager;
    public static final String SELECT_BY_CHANNELID_CHANNELORDERID = "select o from OrderPojo o where channelId=:channelId and channelOrderId=:channelOrderId";
    public static final String SELECT_ORDER_BY_ORDERSTATUS = "select o from OrderPojo o where orderStatus=: status";

    public static final String SELECT_ORDERITEMS_BY_ORDERID = "select o from OrderItemPojo o where orderId=: orderId";
    public static final String SELECT_ORDER_BY_ORDERID = "select o from OrderPojo o where orderId=: orderId";

    public OrderPojo createOrder(OrderPojo orderPojo) {
       entityManager.persist(orderPojo);
       return orderPojo;
    }

    public void createOrderItem(OrderItemPojo orderItemPojo) {
        entityManager.persist(orderItemPojo);
    }

    @Transactional(readOnly = true)
    public OrderPojo getOrderByChannelOrder(String channelOrderId, Long channelId) {
        TypedQuery<OrderPojo> query = entityManager.createQuery(SELECT_BY_CHANNELID_CHANNELORDERID, OrderPojo.class);
        query.setParameter("channelId", channelId);
        query.setParameter("channelOrderId", channelOrderId);
        return query.getResultList().stream().findFirst().orElse(null);
    }

    @Transactional(readOnly = true)
    public List<OrderPojo> getOrderByStatus(OrderStatus status) {
        TypedQuery<OrderPojo> query = entityManager.createQuery(SELECT_ORDER_BY_ORDERSTATUS, OrderPojo.class);
        query.setParameter("status", status);
        return query.getResultList();
    }

    @Transactional(readOnly = true)
    public List<OrderItemPojo> getOrderItemsByOrderId(Long orderId) {
        TypedQuery<OrderItemPojo> query = entityManager.createQuery(SELECT_ORDERITEMS_BY_ORDERID, OrderItemPojo.class);
        query.setParameter("orderId", orderId);
        return query.getResultList();

    }

    @Transactional(readOnly = true)
    public OrderPojo getOrderByOrderId(Long orderId) {
        TypedQuery<OrderPojo> query = entityManager.createQuery(SELECT_ORDER_BY_ORDERID, OrderPojo.class);
        query.setParameter("orderId", orderId);
        return query.getResultList().stream().findFirst().orElse(null);
    }
}
