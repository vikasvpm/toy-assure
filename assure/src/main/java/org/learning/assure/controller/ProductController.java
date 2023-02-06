package org.learning.assure.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.learning.assure.api.ProductApi;
import org.learning.assure.dto.ProductDto;
import org.learning.commons.exception.ApiException;
import org.learning.assure.model.form.ProductForm;
import org.learning.assure.pojo.ProductPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.List;

@Api
@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductApi productApi;

    @Autowired
    private ProductDto productDto;

    @PostMapping(path = "")
    @ApiOperation(value = " Add products")
    public List<ProductPojo> addProducts(@NotNull(message = "CSV file can not be null") @RequestBody MultipartFile productCsvFile,@RequestParam Long clientId) throws ApiException, IOException {
        return productDto.addProducts(productCsvFile, clientId);

    }

    @GetMapping(path = "/product/{id}")
    @ApiOperation(value = "Get Product by Global SKU ID")
    public ProductPojo getProductByGlobalSkuId(@PathVariable Long id) {
        return productApi.getProductByGlobalSkuId(id);
    }

    @GetMapping(path = "")
    @ApiOperation(value = "Get all Products")
    public List<ProductPojo> getAllProducts() {
        return productDto.getAllProducts();
    }


}
