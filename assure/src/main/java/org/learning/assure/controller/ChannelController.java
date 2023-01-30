package org.learning.assure.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.learning.assure.dto.ChannelDto;
import org.learning.assure.exception.ApiException;
import org.learning.assure.model.form.ChannelForm;
import org.learning.assure.pojo.ChannelPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@Api
@RequestMapping("/channel")
public class ChannelController {
    @Autowired
    private ChannelDto channelDto;
    @PostMapping("")
    @ApiOperation(value = "Create a channel")
    public ChannelPojo addChannel(@Valid @RequestBody ChannelForm channelForm) throws ApiException {
        return channelDto.addChannel(channelForm);
    }
}
