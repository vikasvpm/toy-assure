package org.learning.assure.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.learning.assure.api.ProductApi;
import org.learning.assure.dto.ProductDto;
import org.learning.assure.exception.ApiException;
import org.learning.assure.model.form.ProductForm;
import org.learning.assure.pojo.ProductPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api
@RestController
public class ProductController {

    @Autowired
    private ProductApi productApi;

    @Autowired
    private ProductDto productDto;

    @PostMapping(path = "/product/{clientId}")
    @ApiOperation(value = "Create a product")
    public void addProducts(@RequestBody List<ProductForm> productFormList, @PathVariable Long clientId) throws ApiException {
        productDto.addProducts(productFormList, clientId);
    }

    @PutMapping(path = "/product/{clientId}")
    @ApiOperation(value = "Create products in batch")
    public void updateProduct(@RequestBody ProductForm productForm, @PathVariable Long clientId) throws ApiException {
        productDto.updateProduct(productForm, clientId);
    }
    // TODO HANDLE DUPLICACY VALIDATION

    @GetMapping(path = "/product/{id}")
    @ApiOperation(value = "Get Product by Global SKU ID")
    public ProductPojo getProductByGlobalSkuId(@PathVariable Long id) {
        return productApi.getProductByGlobalSkuId(id);
    }

    @GetMapping(path = "/product")
    @ApiOperation(value = "Get all Products")
    public List<ProductPojo> getAllProducts() {
        return productDto.getAllProducts();
    }

    @DeleteMapping(path = "/product/{id}")
    @ApiOperation(value = "Delete product by Global SKU ID")
    public void deleteProductByGlobalSkuId(@PathVariable Long id) {
        productDto.deleteProduct(id);
    }


}
