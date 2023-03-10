package org.learning.assure.api;

import org.learning.assure.dao.InventoryDao;
import org.learning.commons.exception.ApiException;
import org.learning.assure.pojo.InventoryPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class InventoryApi {

    @Autowired
    private InventoryDao inventoryDao;

    public void addInventory(List<InventoryPojo> inventoryPojoList) {
        for(InventoryPojo inventoryPojo : inventoryPojoList) {
            InventoryPojo exists = getByGlobalSkuId(inventoryPojo.getGlobalSkuId());
            if(Objects.isNull(exists)) {
                inventoryDao.addInventory(inventoryPojo);
            }
            else {
                exists.setAvailableQuantity(exists.getAvailableQuantity() + inventoryPojo.getAvailableQuantity());
            }
        }
    }

    @Transactional(readOnly = true)
    public InventoryPojo getByGlobalSkuId(Long globalSkuId) {
        return inventoryDao.getByGlobalSkuId(globalSkuId);
    }
}
