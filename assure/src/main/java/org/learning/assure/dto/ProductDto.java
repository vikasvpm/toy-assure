package org.learning.assure.dto;


import org.learning.assure.api.ProductApi;
import org.learning.assure.api.UserApi;
import org.learning.assure.exception.ApiException;
import org.learning.assure.model.form.ProductForm;
import org.learning.assure.pojo.ProductPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ProductDto {

    @Autowired
    private ProductApi productApi;
    @Autowired
    private UserApi userApi;


    public void deleteProduct(Long id) {
        productApi.deleteProduct(id); //Add validation to check if exists
    }

    public List<ProductPojo> getAllProducts() {
        return productApi.getAllProducts();
    }

    public void addProducts(List<ProductForm> productFormList, Long clientId) throws ApiException {

        validateForClientId(clientId);
        List<ProductPojo> productPojoList = convertListOfProductFormToListOfProductPojo(productFormList, clientId);
        productApi.addProducts(productPojoList);
    }

    private void validateForClientId(Long clientId) throws ApiException {
        userApi.invalidClientCheck(clientId);
    }


    private ProductPojo convertProductFromToProductPojoList(ProductForm productForm, Long clientId) {
        ProductPojo productPojo = new ProductPojo();
        productPojo.setName(productForm.getName());
        productPojo.setDescription(productForm.getDescription());
        productPojo.setClientId(clientId);
        productPojo.setClientSkuId(productForm.getClientSkuId());
        productPojo.setBrandId(productForm.getBrandId());
        return productPojo;
    }

    private List<ProductPojo> convertListOfProductFormToListOfProductPojo(List<ProductForm> productFormList, Long clientId) {
        List<ProductPojo> productPojoList = new ArrayList<>();
        for(ProductForm productForm : productFormList) {
            productPojoList.add(convertProductFromToProductPojoList(productForm, clientId));
        }
        return productPojoList;
    }
}
