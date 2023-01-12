package org.learning.assure.dao;

import org.learning.assure.model.enums.OrderStatus;
import org.learning.assure.pojo.OrderItemPojo;
import org.learning.assure.pojo.OrderPojo;
import org.learning.assure.pojo.ProductPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class OrderDao {

    @PersistenceContext
    EntityManager entityManager;
    public static final String SELECT_BY_CHANNELID_CHANNELORDERID = "select o from OrderPojo o where channelId=:channelId and channelOrderId=:channelOrderId";
    public static final String SELECT_ORDER_BY_ORDERSTATUS = "select o from OrderPojo o where orderStatus=: status";

    public static final String SELECT_ORDERITEMS_BY_ORDERID = "select o from OrderItemPojo o where orderId=: orderId";


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

    public List<OrderPojo> getOrderByStatus(OrderStatus status) {
        TypedQuery<OrderPojo> query = entityManager.createQuery(SELECT_ORDER_BY_ORDERSTATUS, OrderPojo.class);
        query.setParameter("status", status);
        return query.getResultList();
    }

    public List<OrderItemPojo> getOrderItemsByOrderId(Long orderId) {
        TypedQuery<OrderItemPojo> query = entityManager.createQuery(SELECT_ORDERITEMS_BY_ORDERID, OrderItemPojo.class);
        query.setParameter("orderId", orderId);
        return query.getResultList();

    }
}
