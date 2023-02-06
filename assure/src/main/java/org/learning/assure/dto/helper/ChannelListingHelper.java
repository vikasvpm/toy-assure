package org.learning.assure.dto.helper;

import org.learning.assure.model.form.ChannelListingForm;
import org.learning.assure.pojo.ChannelListingPojo;
import org.learning.assure.pojo.ProductPojo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ChannelListingHelper {
    public static List<ChannelListingPojo> convertChannelListingFormListToChannelListingPojoList
            (List<ChannelListingForm> channelListingFormList, Long clientId, Long channelId, Map<String, Long> map) {
        List<ChannelListingPojo> channelListingPojoList = new ArrayList<>();
        for(ChannelListingForm channelListingForm : channelListingFormList) {
            ChannelListingPojo channelListingPojo = convertChannelListingFormToChannelListingPojo(channelListingForm, clientId, channelId, map);
            channelListingPojoList.add(channelListingPojo);
        }
        return channelListingPojoList;
    }

    private static ChannelListingPojo convertChannelListingFormToChannelListingPojo(ChannelListingForm channelListingForm, Long clientId, Long channelId, Map<String, Long> map) {
        ChannelListingPojo channelListingPojo = new ChannelListingPojo();
        channelListingPojo.setChannelId(channelId);
        channelListingPojo.setClientId(clientId);
        channelListingPojo.setGlobalSkuId(map.get(channelListingForm.getClientSkuId()));
        channelListingPojo.setChannelSkuId(channelListingForm.getChannelSkuId());
        return channelListingPojo;
    }
}
