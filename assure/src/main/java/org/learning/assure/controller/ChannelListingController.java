package org.learning.assure.controller;

import io.swagger.annotations.Api;
import org.learning.assure.dto.ChannelListingDto;
import org.learning.assure.exception.ApiException;
import org.learning.assure.model.form.ChannelListingForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api
@RequestMapping("/channel-listing")
public class ChannelListingController {

    @Autowired
    private ChannelListingDto channelListingDto;
    @PostMapping(path = "")
    public ResponseEntity<?> addChannelListing(@RequestBody List<ChannelListingForm> channelListingFormList, @RequestParam Long clientId, @RequestParam Long channelId) throws ApiException {
        channelListingDto.addChannelListing(channelListingFormList, clientId, channelId);
        return new ResponseEntity<>( "Added channel listing successfully", HttpStatus.OK);
    }
}
