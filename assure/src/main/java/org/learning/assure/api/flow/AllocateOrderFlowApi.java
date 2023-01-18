package org.learning.assure.api.flow;

import org.learning.assure.api.BinSkuApi;
import org.learning.assure.pojo.BinSkuPojo;
import org.learning.assure.pojo.InventoryPojo;
import org.learning.assure.pojo.OrderItemPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service

public class AllocateOrderFlowApi {

    @Autowired
    private BinSkuApi binSkuApi;

    @Transactional
    public void allocateOrder(InventoryPojo inventoryPojo, OrderItemPojo orderItemPojo, Long allocated) {
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
