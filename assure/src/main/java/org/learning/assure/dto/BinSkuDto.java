package org.learning.assure.dto;

import org.learning.assure.api.BinApi;
import org.learning.assure.api.BinSkuApi;
import org.learning.assure.api.InventoryApi;
import org.learning.assure.api.ProductApi;
import org.learning.assure.dto.helper.BinSkuHelper;
import org.learning.assure.dto.helper.InventoryHelper;
import org.learning.assure.exception.ApiException;
import org.learning.assure.model.form.BinSkuForm;
import org.learning.assure.pojo.BinSkuPojo;
import org.learning.assure.pojo.InventoryPojo;
import org.learning.assure.pojo.ProductPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BinSkuDto {
    @Autowired
    private ProductApi productApi;

    @Autowired
    private BinApi binApi;

    @Autowired
    private BinSkuApi binSkuApi;

    @Autowired
    private InventoryApi inventoryApi;

    public void addBinSkus(List<BinSkuForm> binSkuFormList, Long clientId) {
        validateForClientSkuId(binSkuFormList, clientId);
        validateForBinId(binSkuFormList);
        Map<String, Long> map = mapToGlobalSkuId(binSkuFormList, clientId);
        List<BinSkuPojo> binSkuPojoList = BinSkuHelper.convertBinSkuFormListToBinSkuPojoList(binSkuFormList, clientId, map);
        binSkuApi.addBinSkus(binSkuPojoList);
        List<InventoryPojo> inventoryPojoList = InventoryHelper.convertToInventoryPojoList(binSkuFormList, map);
        inventoryApi.addInventory(inventoryPojoList);

    }

    private Map<String, Long> mapToGlobalSkuId(List<BinSkuForm> binSkuFormList, Long clientId) {
        Map<String, Long> map = new HashMap<>();
        for(BinSkuForm binSkuForm : binSkuFormList) {
            ProductPojo productPojo = productApi.getProductByClientIdAndClientSkuId(clientId, binSkuForm.getClientSkuId());
            map.put(binSkuForm.getClientSkuId(), productPojo.getGlobalSkuId());
        }
        return map;
    }


    private void validateForBinId(List<BinSkuForm> binSkuFormList) {
        Set<Long> binIdSet = new HashSet<>();
        binSkuFormList.stream().map(BinSkuForm::getBinId)
                .forEach(binId -> {
                    if(binApi.getBinByBinId(binId) == null) {
                        try {
                            throw new ApiException("Bin with id " + binId + " does not exist");
                        } catch (ApiException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
    }

    private void validateForClientSkuId(List<BinSkuForm> binSkuFormList, Long clientId) {
        Set<String> clientSkuIdSet = new HashSet<>();
        binSkuFormList.stream().map(BinSkuForm::getClientSkuId)
                .forEach(clientSkuId -> {
                    if(productApi.getProductByClientIdAndClientSkuId(clientId,clientSkuId) == null) {
                        try {
                            throw new ApiException("Client SKU ID does not exist in the system");
                        } catch (ApiException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    if(clientSkuIdSet.contains(clientSkuId)) {
                        try {
                            throw new ApiException("Duplicate Client SKU ID");
                        } catch (ApiException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    clientSkuIdSet.add(clientSkuId);
                    
                });
    }


}
