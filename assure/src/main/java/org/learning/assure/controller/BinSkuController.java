package org.learning.assure.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.learning.assure.dto.BinSkuDto;
import org.learning.assure.pojo.BinSkuPojo;
import org.learning.commons.exception.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@Api
@RequestMapping("/binsku")
public class BinSkuController {

    @Autowired
    private BinSkuDto binSkuDto;


    @PostMapping(path = "")
    @ApiOperation(value = "Add Bin SKUs")
    public List<BinSkuPojo> addBinSkus(@RequestBody MultipartFile binSkuCsvFile, @RequestParam Long clientId) throws ApiException, IOException {
        List<BinSkuPojo> binSkuPojoList = binSkuDto.addBinSkus(binSkuCsvFile, clientId);
        return binSkuPojoList;

    }
}
