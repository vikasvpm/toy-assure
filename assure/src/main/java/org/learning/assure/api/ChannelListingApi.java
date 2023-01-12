package org.learning.assure.api;

import org.learning.assure.dao.ChannelListingDao;
import org.learning.assure.model.form.ChannelOrderForm;
import org.learning.assure.pojo.ChannelListingPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class ChannelListingApi {

    @Autowired
    private ChannelListingDao channelListingDao;
    public void addChannelListing(List<ChannelListingPojo> channelListingPojoList) {
        for(ChannelListingPojo channelListingPojo : channelListingPojoList) {
            channelListingDao.addChannelListing(channelListingPojo);
        }
    }

    public ChannelListingPojo getChannelListingToMapGlobalSkuId(Long clientId, Long channelId, String channelSkuId) {
        return channelListingDao.getChannelListingToMapGlobalSkuId(clientId, channelId, channelSkuId );
    }
}
