package org.learning.assure.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.learning.assure.dto.BinSkuDto;
import org.learning.assure.model.form.BinSkuForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api
public class BinSkuController {

    @Autowired
    private BinSkuDto binSkuDto;


    @PostMapping(path = "/binsku/{clientId}")
    @ApiOperation(value = "Add Bin SKUs")
    public void addBinSkus(@RequestBody List<BinSkuForm> binSkuFormList, @PathVariable Long clientId) {
        binSkuDto.addBinSkus(binSkuFormList, clientId);

    }
}
