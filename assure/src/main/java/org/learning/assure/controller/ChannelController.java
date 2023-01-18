package org.learning.assure.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.learning.assure.dto.ChannelDto;
import org.learning.assure.exception.ApiException;
import org.learning.assure.model.form.ChannelForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api
@RequestMapping("/channel")
public class ChannelController {
    @Autowired
    private ChannelDto channelDto;
    @PostMapping("")
    @ApiOperation(value = "Create a channel")
    public ResponseEntity<?> addChannel(@RequestBody ChannelForm channelForm) throws ApiException {
        channelDto.addChannel(channelForm);
        return new ResponseEntity<>( "Added channel successfully", HttpStatus.CREATED);
    }
}
