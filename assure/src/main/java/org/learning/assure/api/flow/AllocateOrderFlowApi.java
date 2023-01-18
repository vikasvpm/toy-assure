package org.learning.assure.api.flow;

import org.learning.assure.api.BinSkuApi;
import org.learning.assure.api.InventoryApi;
import org.learning.assure.api.OrderApi;
import org.learning.assure.model.enums.OrderStatus;
import org.learning.assure.pojo.BinSkuPojo;
import org.learning.assure.pojo.InventoryPojo;
import org.learning.assure.pojo.OrderItemPojo;
import org.learning.assure.pojo.OrderPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service

public class AllocateOrderFlowApi {

    @Autowired
    private BinSkuApi binSkuApi;

    @Autowired
    private OrderApi orderApi;

    @Autowired
    private InventoryApi inventoryApi;


    @Transactional
    public void allocateOrder() {
        List<OrderPojo> createdOrders = orderApi.getOrdersByStatus(OrderStatus.CREATED);
        for(OrderPojo orderPojo : createdOrders) {
            Boolean completeAllocate = true;
            List<OrderItemPojo> orderedItems = orderApi.getOrderItemsByOrderId(orderPojo.getOrderId());
            for(OrderItemPojo orderItemPojo : orderedItems) {
                if(orderItemPojo.getOrderedQuantity() > orderItemPojo.getAllocatedQuantity()) {
                    InventoryPojo inventoryPojo = inventoryApi.getByGlobalSkuId(orderItemPojo.getGlobalSkuId());
                    Long allocated;
                    if(inventoryPojo.getAvailableQuantity() >= orderItemPojo.getOrderedQuantity() - orderItemPojo.getAllocatedQuantity()) {
                        allocated = orderItemPojo.getOrderedQuantity() - orderItemPojo.getAllocatedQuantity();
                    }
                    else {
                        allocated = inventoryPojo.getAvailableQuantity();
                        completeAllocate = false;
                    }
                    inventoryPojo.setAvailableQuantity(inventoryPojo.getAvailableQuantity() - allocated);
                    inventoryPojo.setAllocatedQuantity(inventoryPojo.getAllocatedQuantity() + allocated);
                    orderItemPojo.setAllocatedQuantity(orderItemPojo.getAllocatedQuantity() + allocated);
                    List<BinSkuPojo> binSkuPojoList = binSkuApi.getListByGlobalSkuId(orderItemPojo.getGlobalSkuId());
                    for(BinSkuPojo binSkuPojo : binSkuPojoList) {
                        if(binSkuPojo.getQuantity() > allocated) {
                            binSkuPojo.setQuantity(binSkuPojo.getQuantity() - allocated);
                            break;
                        }
                        else {
                            allocated = allocated - binSkuPojo.getQuantity();
                            binSkuApi.deleteByBinSkuId(binSkuPojo.getBinSkuId());
                        }
                    }
                }
            }
            if(completeAllocate.equals(true)) {
                orderPojo.setOrderStatus(OrderStatus.ALLOCATED);
            }
        }
    }
}
