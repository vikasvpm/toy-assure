package org.learning.assure.dao;

import org.learning.assure.pojo.ChannelPojo;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

@Repository
@Transactional
public class ChannelDao {

    @PersistenceContext
    EntityManager entityManager;

    public static final String SELECT_BY_NAME = "select c from ChannelPojo c where name=:name";

    public static final String SELECT_BY_ID = "select c from ChannelPojo c where channelId=:channelId";
    public static final String SELECT_ALL = "select c from ChannelPojo c";


    public ChannelPojo addChannel(ChannelPojo channelPojo) {
        entityManager.persist(channelPojo);
        return channelPojo;
    }

    @Transactional(readOnly = true)
    public ChannelPojo getChannelByName(String name) {
        TypedQuery<ChannelPojo> query = entityManager.createQuery(SELECT_BY_NAME, ChannelPojo.class);
        query.setParameter("name", name);
        return query.getResultList().stream().findFirst().orElse(null);
    }

    @Transactional(readOnly = true)
    public ChannelPojo getDefault() {
        TypedQuery<ChannelPojo> query = entityManager.createQuery(SELECT_ALL, ChannelPojo.class);
        return query.getResultList().stream().findFirst().orElse(null);
    }

    @Transactional(readOnly = true)
    public ChannelPojo getChannelById(Long channelId) {
        TypedQuery<ChannelPojo> query = entityManager.createQuery(SELECT_BY_ID, ChannelPojo.class);
        query.setParameter("channelId", channelId);
        return query.getResultList().stream().findFirst().orElse(null);
    }
}
