package org.learning.assure.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.learning.assure.dto.OrderDto;
import org.learning.assure.exception.ApiException;
import org.learning.assure.model.form.ChannelOrderForm;
import org.learning.assure.model.form.InternalOrderForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderDto orderDto;
    @PostMapping(path = "/internal")
    @ApiOperation(value = "Create Internal Order")
    public ResponseEntity<?> createInternalOrder(
            @RequestBody List<InternalOrderForm> internalOrderFormList, @RequestParam Long clientId,
            @RequestParam String channelOrderId, @RequestParam Long customerId) throws ApiException {
        orderDto.createInternalOrder(internalOrderFormList, clientId, channelOrderId, customerId);
        return new ResponseEntity<>( "Created Internal order successfully", HttpStatus.CREATED);

    }

    @PostMapping(path = "/channel")
    @ApiOperation(value = "Create Channel Order")
    public ResponseEntity<?> createChannelOrder(@RequestBody List<ChannelOrderForm> channelOrderFormList, @RequestParam Long clientId,
                                   @RequestParam String channelOrderId, @RequestParam Long customerId,
                                   @RequestParam String channelName) throws ApiException {
        orderDto.createChannelOrder(channelOrderFormList, clientId, channelOrderId, customerId, channelName);
        return new ResponseEntity<>( "Created Channel Order successfully", HttpStatus.CREATED);

    }

    @PostMapping(path = "/allocate")
    @ApiOperation(value ="Allocate created orders")
    public ResponseEntity<?> allocateOrder() {
        orderDto.allocateOrder();
        return new ResponseEntity<>( "Allocated orders successfully", HttpStatus.CREATED);

    }



}
