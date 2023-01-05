package org.learning.assure.dto;

import org.learning.assure.api.BinApi;
import org.learning.assure.api.BinSkuApi;
import org.learning.assure.api.ProductApi;
import org.learning.assure.dto.helper.BinSkuHelper;
import org.learning.assure.exception.ApiException;
import org.learning.assure.model.form.BinSkuForm;
import org.learning.assure.pojo.BinSkuPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class BinSkuDto {
    @Autowired
    private ProductApi productApi;

    @Autowired
    private BinApi binApi;

    @Autowired
    private BinSkuApi binSkuApi;

    @Autowired
    private BinSkuHelper binSkuHelper;
    public void addBinSkus(List<BinSkuForm> binSkuFormList, Long clientId) {
        validateForClientSkuId(binSkuFormList, clientId);
        validateForBinId(binSkuFormList);
        List<BinSkuPojo> binSkuPojoList = binSkuHelper.convertBinSkuFormListToBinSkuPojoList(binSkuFormList,clientId);
        binSkuApi.addBinSkus(binSkuPojoList);

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
