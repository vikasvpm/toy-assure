package org.learning.assure.controller;

import io.swagger.annotations.Api;
import org.learning.assure.dto.ChannelListingDto;
import org.learning.commons.exception.ApiException;
import org.learning.assure.model.form.ChannelListingForm;
import org.learning.assure.pojo.ChannelListingPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@Api
@RequestMapping("/channel-listing")
public class ChannelListingController {

    @Autowired
    private ChannelListingDto channelListingDto;
    @PostMapping(path = "")
    public List<ChannelListingPojo> addChannelListing(@RequestBody MultipartFile channelListingCsv, @RequestParam Long clientId, @RequestParam Long channelId) throws ApiException, IOException {
        return channelListingDto.addChannelListing(channelListingCsv, clientId, channelId);
    }
}
