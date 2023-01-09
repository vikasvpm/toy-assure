package org.learning.assure.dto.helper;

import org.learning.assure.model.form.ChannelListingForm;
import org.learning.assure.pojo.ChannelListingPojo;
import org.learning.assure.pojo.ProductPojo;

import java.util.ArrayList;
import java.util.List;

public class ChannelListingHelper {
    public static List<ChannelListingPojo> convertChannelListingFormListToChannelListingPojoList
            (List<ChannelListingForm> channelListingFormList, Long clientId, Long channelId) {
        List<ChannelListingPojo> channelListingPojoList = new ArrayList<>();
        for(ChannelListingForm channelListingForm : channelListingFormList) {
            ChannelListingPojo channelListingPojo = convertChannelListingFormToChannelListingPojo(channelListingForm, clientId, channelId);
            channelListingPojoList.add(channelListingPojo);
        }
        return channelListingPojoList;
    }

    private static ChannelListingPojo convertChannelListingFormToChannelListingPojo(ChannelListingForm channelListingForm, Long clientId, Long channelId) {
        ChannelListingPojo channelListingPojo = new ChannelListingPojo();
        channelListingPojo.setChannelId(channelId);
        channelListingPojo.setClientId(clientId);
        channelListingPojo.setGlobalSkuId(channelListingForm.getGlobalSkuId());
        channelListingPojo.setChannelSkuId(channelListingForm.getChannelSkuId());
        return channelListingPojo;
    }
}
