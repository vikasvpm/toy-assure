package org.learning.assure.api;

import org.learning.assure.dao.BinDao;
import org.learning.assure.pojo.BinPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BinApi {
    @Autowired
    private BinDao binDao;

    public BinPojo addBin(BinPojo binPojo) {
        binDao.addBin(binPojo);
        return binPojo;
    }

    @Transactional(readOnly = true)
    public BinPojo getBinByBinId(Long binId) {
        return binDao.getBinById(binId);
    }

}
