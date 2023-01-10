package org.learning.assure.dto.helper;

import org.learning.assure.model.form.BinSkuForm;
import org.learning.assure.pojo.BinSkuPojo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BinSkuHelper {

    public static List<BinSkuPojo> convertBinSkuFormListToBinSkuPojoList(List<BinSkuForm> binSkuFormList, Long clientId, Map<String, Long> map) {
        List<BinSkuPojo> binSkuPojoList = new ArrayList<>();
        for(BinSkuForm binSkuForm : binSkuFormList) {
            BinSkuPojo binSkuPojo = convertBinSkuFormToBinSkuPojo(binSkuForm, clientId, map);
            binSkuPojoList.add(binSkuPojo);
        }
        return binSkuPojoList;
    }

    public static BinSkuPojo convertBinSkuFormToBinSkuPojo(BinSkuForm binSkuForm, Long clientId, Map<String, Long> map) {
        BinSkuPojo binSkuPojo = new BinSkuPojo();
        binSkuPojo.setGlobalSkuId(map.get(binSkuForm.getClientSkuId()));
        binSkuPojo.setBinId(binSkuForm.getBinId());
        binSkuPojo.setQuantity(binSkuForm.getQuantity());
        return binSkuPojo;
    }


}
