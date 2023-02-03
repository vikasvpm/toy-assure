package org.learning.assure.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.learning.assure.dto.BinDto;
import org.learning.commons.exception.ApiException;
import org.learning.assure.pojo.BinPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.ws.Response;
import java.util.List;

@Api
@RestController
@RequestMapping("/bin")
public class BinController {

    @Autowired
    private BinDto binDto;
    @ApiOperation(value = "Add Bins")
    @PostMapping()
    public List<BinPojo> addBins(@RequestParam Long noOfBins) throws ApiException {
        List<BinPojo> binPojoList = binDto.addBins(noOfBins);
        return binPojoList;
    }
}
