package org.learning.assure.controller;

import io.swagger.annotations.Api;
import org.learning.assure.api.ChannelListingApi;
import org.learning.assure.dto.ChannelListingDto;
import org.learning.assure.exception.ApiException;
import org.learning.assure.model.form.ChannelListingForm;
import org.learning.assure.pojo.ChannelListingPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Api
public class ChannelListingController {

    @Autowired
    private ChannelListingDto channelListingDto;
    @PostMapping(path = "/channel-listing")
    public void addChannelListing(@RequestBody List<ChannelListingForm> channelListingFormList, @RequestParam Long clientId, @RequestParam Long channelId) throws ApiException {
        channelListingDto.addChannelListing(channelListingFormList, clientId, channelId);
    }
}
