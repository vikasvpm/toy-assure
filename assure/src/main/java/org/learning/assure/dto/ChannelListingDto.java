package org.learning.assure.dto;

import org.learning.assure.api.ChannelApi;
import org.learning.assure.api.ChannelListingApi;
import org.learning.assure.api.UserApi;
import org.learning.assure.dto.helper.ChannelListingHelper;
import org.learning.assure.exception.ApiException;
import org.learning.assure.model.form.ChannelListingForm;
import org.learning.assure.pojo.ChannelListingPojo;
import org.learning.assure.pojo.ChannelPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChannelListingDto {

    @Autowired
    private UserApi userApi;

    @Autowired
    private ChannelApi channelApi;

    @Autowired
    private ChannelListingApi channelListingApi;
    public void addChannelListing(List<ChannelListingForm> channelListingFormList, Long clientId, Long channelId) throws ApiException {
        validateClientIdAndChannelId(clientId, channelId);
        List<ChannelListingPojo> channelListingPojoList = ChannelListingHelper.convertChannelListingFormListToChannelListingPojoList(channelListingFormList, clientId, channelId);
        channelListingApi.addChannelListing(channelListingPojoList);
    }

    private void validateClientIdAndChannelId(Long clientId, Long channelId) throws ApiException {
        userApi.invalidClientCheck(clientId);
        ChannelPojo channelPojo = channelApi.getChannelById(channelId);
        if(channelPojo == null) {
            throw new ApiException("Channel with channelId " + channelId + " does not exist");
        }
    }
}
