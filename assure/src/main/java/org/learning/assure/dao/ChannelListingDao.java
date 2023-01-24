package org.learning.assure.dao;

import org.learning.assure.pojo.ChannelListingPojo;
import org.learning.assure.pojo.ProductPojo;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

@Repository
@Transactional
public class ChannelListingDao {

    @PersistenceContext
    EntityManager entityManager;

    public static final String SELECT_BY_CLIENTID_CHANNELID_CHANNELSKUID = "select c from ChannelListingPojo c where clientId=:clientId and channelId=:channelId and channelSkuId=:channelSkuId";

    public void addChannelListing(ChannelListingPojo channelListingPojo) {
        entityManager.persist(channelListingPojo);
    }

    @Transactional(readOnly = true)
    public ChannelListingPojo getChannelListingToMapGlobalSkuId(Long clientId, Long channelId, String channelSkuId) {
        TypedQuery<ChannelListingPojo> query = entityManager.createQuery(SELECT_BY_CLIENTID_CHANNELID_CHANNELSKUID, ChannelListingPojo.class);
        query.setParameter("clientId", clientId);
        query.setParameter("channelId", channelId);
        query.setParameter("channelSkuId", channelSkuId);
        return query.getResultList().stream().findFirst().orElse(null);
    }
}
