package org.learning.assure.dao;

import org.learning.assure.pojo.ChannelListingPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class ChannelListingDao {

    @PersistenceContext
    EntityManager entityManager;
    public void addChannelListing(ChannelListingPojo channelListingPojo) {
        entityManager.persist(channelListingPojo);
    }
}
