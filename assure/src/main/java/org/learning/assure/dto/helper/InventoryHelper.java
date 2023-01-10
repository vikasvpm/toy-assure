package org.learning.assure.dto.helper;

import org.learning.assure.model.form.BinSkuForm;
import org.learning.assure.pojo.InventoryPojo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class InventoryHelper {
    public static List<InventoryPojo> convertToInventoryPojoList(List<BinSkuForm> binSkuFormList, Map<String, Long> map) {
        List<InventoryPojo> inventoryPojoList = new ArrayList<>();
        for(BinSkuForm binSkuForm : binSkuFormList) {
            InventoryPojo inventoryPojo = new InventoryPojo();
            inventoryPojo.setAvailableQuantity(binSkuForm.getQuantity());
            inventoryPojo.setAllocatedQuantity(0l);
            inventoryPojo.setFulfilledQuantity(0l);
            inventoryPojo.setGlobalSkuId(map.get(binSkuForm.getClientSkuId()));
            inventoryPojoList.add(inventoryPojo);
        }
        return inventoryPojoList;
    }
}
