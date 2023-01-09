package org.learning.assure.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.learning.assure.dto.OrderDto;
import org.learning.assure.exception.ApiException;
import org.learning.assure.model.form.InternalOrderForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Api
public class OrderController {
    @Autowired
    private OrderDto orderDto;
    @PostMapping(path = "order/internal")
    @ApiOperation(value = "Create Internal Order")
    public void createInternalOrder(
            @RequestBody List<InternalOrderForm> internalOrderFormList, @RequestParam Long clientId,
            @RequestParam String channelOrderId, @RequestParam Long customerId) throws ApiException {
        orderDto.createInternalOrder(internalOrderFormList, clientId, channelOrderId, customerId);

    }

}
