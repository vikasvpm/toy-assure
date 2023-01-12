package org.learning.assure.api;

import org.learning.assure.dao.InventoryDao;
import org.learning.assure.pojo.InventoryPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class InventoryApi {

    @Autowired
    private InventoryDao inventoryDao;

    public void addInventory(List<InventoryPojo> inventoryPojoList) {
        for(InventoryPojo inventoryPojo : inventoryPojoList) {
            InventoryPojo exists = getByGlobalSkuId(inventoryPojo.getGlobalSkuId());
            if(exists == null) {
                inventoryDao.addInventory(inventoryPojo);
            }
            else {
                exists.setAvailableQuantity(exists.getAvailableQuantity() + inventoryPojo.getAvailableQuantity());
            }
        }
    }

    private InventoryPojo getByGlobalSkuId(Long globalSkuId) {
        return inventoryDao.getByGlobalSkuId(globalSkuId);
    }
}
