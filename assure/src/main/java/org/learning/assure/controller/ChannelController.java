package org.learning.assure.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.learning.assure.dto.ChannelDto;
import org.learning.assure.exception.ApiException;
import org.learning.assure.model.form.ChannelForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api
public class ChannelController {
    @Autowired
    private ChannelDto channelDto;
    @PostMapping("/channel")
    @ApiOperation(value = "Create a channel")
    public void addChannel(@RequestBody ChannelForm channelForm) throws ApiException {
        channelDto.addChannel(channelForm);
    }
}
