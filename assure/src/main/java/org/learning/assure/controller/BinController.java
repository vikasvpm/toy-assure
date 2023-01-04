package org.learning.assure.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.learning.assure.dto.BinDto;
import org.learning.assure.exception.ApiException;
import org.learning.assure.pojo.BinPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api
@RestController
public class BinController {

    @Autowired
    private BinDto binDto;
    @ApiOperation(value = "Add Bins")
    @PostMapping(path = "/bin")
    public List<BinPojo> addBins(@RequestParam Long noOfBins) throws ApiException {
        return binDto.addBins(noOfBins);
    }
}
