package org.learning.assure.api;

import org.learning.assure.dao.BinSkuDao;
import org.learning.assure.pojo.BinSkuPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class BinSkuApi {

    @Autowired
    private BinSkuDao binSkuDao;

    public List<BinSkuPojo> addBinSkus(List<BinSkuPojo> binSkuPojoList) {
        for(BinSkuPojo binSkuPojo : binSkuPojoList) {
            BinSkuPojo exists = getByBinIdAndBinSkuId(binSkuPojo.getBinId(), binSkuPojo.getGlobalSkuId());
            if(exists == null) {
                binSkuDao.addBinSku(binSkuPojo);
            }
            else {
                exists.setQuantity(exists.getQuantity() + binSkuPojo.getQuantity());
            }
        }
        return binSkuPojoList;
    }

    @Transactional(readOnly = true)
    private BinSkuPojo getByBinIdAndBinSkuId(Long binId, Long globalSkuId) {
        return binSkuDao.getByBinIdAndGlobalSkuId(binId, globalSkuId);
    }
    @Transactional(readOnly = true)
    public List<BinSkuPojo> getListByGlobalSkuId(Long globalSkuId) {
        return binSkuDao.getListByGlobalSkuId(globalSkuId);
    }
    public void deleteByBinSkuId(Long binSkuId) {
        binSkuDao.deleteByBinSkuId(binSkuId);
    }
}
