package org.learning.assure.api;

import org.learning.assure.dao.BinDao;
import org.learning.assure.pojo.BinPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class BinApi {
    @Autowired
    private BinDao binDao;

    @Transactional
    public BinPojo addBin(BinPojo binPojo) {
        binDao.addBin(binPojo);
        return binPojo;
    }

    @Transactional
    public BinPojo getBinByBinId(Long binId) {
        return binDao.getBinById(binId);
    }

}
