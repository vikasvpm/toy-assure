package org.learning.assure.api;

import org.learning.assure.dao.BinSkuDao;
import org.learning.assure.pojo.BinSkuPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class BinSkuApi {

    @Autowired
    private BinSkuDao binSkuDao;

    public void addBinSkus(List<BinSkuPojo> binSkuPojoList) {
        for(BinSkuPojo binSkuPojo : binSkuPojoList) {
            BinSkuPojo exists = checkIfExists(binSkuPojo.getBinId(), binSkuPojo.getGlobalSkuId());
            if(exists == null) {
                binSkuDao.addBinSku(binSkuPojo);
            }
            else {
                exists.setQuantity(exists.getQuantity() + binSkuPojo.getQuantity());
            }
        }
    }

    private BinSkuPojo checkIfExists(Long binId, Long globalSkuId) {
        return binSkuDao.getByBinIdAndGlobalSkuId(binId, globalSkuId);
    }
}
