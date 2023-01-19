package org.learning.assure.dto;

import org.learning.assure.api.BinApi;
import org.learning.assure.exception.ApiException;
import org.learning.assure.pojo.BinPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class BinDto {
    @Autowired
    private BinApi binApi;
    public List<BinPojo> addBins(Long noOfBins) throws ApiException {
        checkValidNumber(noOfBins);
        List<BinPojo> binPojoList = new ArrayList<>();
        for(Long i = 1l; i <= noOfBins; i++) { // TODO meaningful var names
            binPojoList.add(binApi.addBin(new BinPojo()));
        }
        return binPojoList;

    }

    private void checkValidNumber(Long noOfBins) throws ApiException {
        if(noOfBins < 1l) {
            throw new ApiException("Number of bins to be created can not be 0 or negative");
        }
    }
}
