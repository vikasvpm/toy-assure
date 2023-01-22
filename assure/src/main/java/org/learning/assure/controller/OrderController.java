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
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;
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
            @RequestBody MultipartFile internalOrderCsv, @RequestParam Long clientId,
            @RequestParam String channelOrderId, @RequestParam Long customerId) throws ApiException, IOException {
        orderDto.createInternalOrder(internalOrderCsv, clientId, channelOrderId, customerId);
        return new ResponseEntity<>( "Created Internal order successfully", HttpStatus.OK);

    }

    @PostMapping(path = "/channel")
    @ApiOperation(value = "Create Channel Order")
    public ResponseEntity<?> createChannelOrder(@RequestBody MultipartFile channelOrderCsv, @RequestParam Long clientId,
                                   @RequestParam String channelOrderId, @RequestParam Long customerId,
                                   @RequestParam String channelName) throws ApiException, IOException {
        orderDto.createChannelOrder(channelOrderCsv, clientId, channelOrderId, customerId, channelName);
        return new ResponseEntity<>( "Created Channel Order successfully", HttpStatus.OK);

    }

    @PostMapping(path = "/allocate")
    @ApiOperation(value ="Allocate any created order")
    public ResponseEntity<?> allocateOrder(@RequestParam Long orderId) throws ApiException {
        orderDto.allocateOrder(orderId);
        return new ResponseEntity<>( "Allocated orders successfully", HttpStatus.OK);

    }

    @PostMapping(path = "/fulfill")
    @ApiOperation(value = "Fulfill any allocated order")
    public void fulfillOrder(@RequestParam Long orderId, HttpServletResponse response) throws ApiException {
        orderDto.fulfillOrder(orderId, response);
    }


}
