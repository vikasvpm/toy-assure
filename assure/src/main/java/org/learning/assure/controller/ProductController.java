package org.learning.assure.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.learning.assure.api.ProductApi;
import org.learning.assure.pojo.ProductPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api
@RestController
public class ProductController {

    @Autowired
    private ProductApi productApi;

    @PostMapping(path = "/product")
    @ApiOperation(value = "Create a product")
    public void addProduct(@RequestBody ProductPojo productPojo) {
        productApi.addProduct(productPojo);
    }

    @GetMapping(path = "/product/{id}")
    @ApiOperation(value = "Get Product by Global SKU ID")
    public ProductPojo getProductByGlobalSkuId(@PathVariable Long id) {
        return productApi.getProductByGlobalSkuId(id);
    }

    @GetMapping(path = "/product")
    @ApiOperation(value = "Get all Prducts")
    public List<ProductPojo> getAllProducts() {
        return productApi.getAllProducts();
    }

    @DeleteMapping(path = "/product/{id}")
    @ApiOperation(value = "Delete product by Global SKU ID")
    public void deleteProductByGlobalSkuId(@PathVariable Long id) {
        productApi.deleteProduct(id);
    }

}
