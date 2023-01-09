package org.learning.assure.dto.helper;

import org.learning.assure.api.ProductApi;
import org.learning.assure.model.form.BinSkuForm;
import org.learning.assure.pojo.BinSkuPojo;
import org.learning.assure.pojo.ProductPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BinSkuHelper {

    @Autowired
    private ProductApi productApi;
    // TODO : refactor to only have static functions and no autowiring
    public List<BinSkuPojo> convertBinSkuFormListToBinSkuPojoList(List<BinSkuForm> binSkuFormList, Long clientId) {
        List<BinSkuPojo> binSkuPojoList = new ArrayList<>();
        for(BinSkuForm binSkuForm : binSkuFormList) {
            BinSkuPojo binSkuPojo = convertBinSkuFormToBinSkuPojo(binSkuForm, clientId);
            binSkuPojoList.add(binSkuPojo);
        }
        return binSkuPojoList;
    }

    private BinSkuPojo convertBinSkuFormToBinSkuPojo(BinSkuForm binSkuForm, Long clientId) {
        BinSkuPojo binSkuPojo = new BinSkuPojo();
        binSkuPojo.setGlobalSkuId(setGlobalSkuId(clientId, binSkuForm.getClientSkuId()));
        binSkuPojo.setBinId(binSkuForm.getBinId());
        binSkuPojo.setQuantity(binSkuForm.getQuantity());
        return binSkuPojo;
    }

    private Long setGlobalSkuId(Long clientId, String clientSkuId) {
        return productApi.getProductByClientIdAndClientSkuId(clientId, clientSkuId).getGlobalSkuId();
    }

}
