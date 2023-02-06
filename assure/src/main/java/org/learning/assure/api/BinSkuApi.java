package org.learning.assure.api;

import org.learning.assure.dao.BinSkuDao;
import org.learning.assure.pojo.BinSkuPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class BinSkuApi {

    @Autowired
    private BinSkuDao binSkuDao;

    public List<BinSkuPojo> addBinSkus(List<BinSkuPojo> binSkuPojoList) {
        List<BinSkuPojo> created = new ArrayList<>();
        for(BinSkuPojo binSkuPojo : binSkuPojoList) {
            BinSkuPojo exists = getByBinIdAndBinSkuId(binSkuPojo.getBinId(), binSkuPojo.getGlobalSkuId());
            if(Objects.isNull(exists)) {
                created.add(binSkuDao.addBinSku(binSkuPojo));
            }
            else {
                exists.setQuantity(exists.getQuantity() + binSkuPojo.getQuantity());
                created.add(exists);
            }
        }
        return created;
    }

    @Transactional(readOnly = true)
    public BinSkuPojo getByBinIdAndBinSkuId(Long binId, Long globalSkuId) {
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
