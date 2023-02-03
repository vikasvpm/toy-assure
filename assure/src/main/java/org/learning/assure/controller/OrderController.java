package org.learning.assure.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.learning.assure.dto.OrderDto;
import org.learning.commons.exception.ApiException;

import org.learning.assure.pojo.OrderPojo;
import org.learning.commons.model.OrderForm;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@Api
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderDto orderDto;
    @PostMapping(path = "/internal")
    @ApiOperation(value = "Create Internal Order")
    public OrderPojo createInternalOrder(
            @RequestBody MultipartFile internalOrderCsv, @RequestParam Long clientId,
            @RequestParam String channelOrderId, @RequestParam Long customerId) throws ApiException, IOException {
        return orderDto.createInternalOrder(internalOrderCsv, clientId, channelOrderId, customerId);

    }

    @PostMapping(path = "/channel")
    @ApiOperation(value = "Create Channel Order")
    public OrderPojo createChannelOrder(@RequestBody OrderForm orderForm) throws ApiException, IOException {
        return orderDto.createChannelOrder(orderForm);

    }

    @PostMapping(path = "/allocate")
    @ApiOperation(value ="Allocate any created order")
    public OrderPojo allocateOrder(@RequestParam Long orderId) throws ApiException {
        return orderDto.allocateOrder(orderId);

    }

    @PostMapping(path = "/fulfill")
    @ApiOperation(value = "Fulfill any allocated order")
    public OrderPojo fulfillOrder(@RequestParam Long orderId, HttpServletResponse response) throws ApiException {
        return orderDto.fulfillOrder(orderId, response);
    }


}
