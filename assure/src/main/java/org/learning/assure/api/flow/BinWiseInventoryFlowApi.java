package org.learning.assure.api.flow;

import org.learning.assure.api.BinSkuApi;
import org.learning.assure.api.InventoryApi;
import org.learning.assure.pojo.BinSkuPojo;
import org.learning.assure.pojo.InventoryPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BinWiseInventoryFlowApi {

    @Autowired
    private BinSkuApi binSkuApi;

    @Autowired
    private InventoryApi inventoryApi;

    @Transactional
    public List<BinSkuPojo> addBinWiseInventory(List<BinSkuPojo> binSkuPojoList, List<InventoryPojo> inventoryPojoList) {
        List<BinSkuPojo> createdBinSkuPojoList = binSkuApi.addBinSkus(binSkuPojoList);
        inventoryApi.addInventory(inventoryPojoList);
        return createdBinSkuPojoList;
    }
}
