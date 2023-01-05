package org.learning.assure.dto;


import org.learning.assure.api.ProductApi;
import org.learning.assure.api.UserApi;
import org.learning.assure.dto.helper.ProductHelper;
import org.learning.assure.exception.ApiException;
import org.learning.assure.model.form.ProductForm;
import org.learning.assure.pojo.ProductPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        validateForDuplicate(productFormList, clientId);
        List<ProductPojo> productPojoList = ProductHelper.convertListOfProductFormToListOfProductPojo(productFormList, clientId);
        productApi.addProducts(productPojoList);
    }

    private void validateForDuplicate(List<ProductForm> productFormList, Long clientId) {
        Set<String> clientSkuIdSet = new HashSet<>();
        productFormList.stream().map(ProductForm :: getClientSkuId)
                .forEach(clientSkuId -> {
                    if(clientSkuIdSet.contains(clientSkuId)) {
                        try {
                            throw new ApiException("Duplicate Client SKUs in the upload");
                        } catch (ApiException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    clientSkuIdSet.add(clientSkuId);
                    if(productApi.getProductByClientIdAndClientSkuId(clientId, clientSkuId) != null) {
                        try {
                            throw new ApiException("This record already exists in System");
                        } catch (ApiException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });

    }

    private void validateForClientId(Long clientId) throws ApiException {
        userApi.invalidClientCheck(clientId);
    }

    public void updateProduct(ProductForm productForm, Long clientId) throws ApiException {
        validateForClientId(clientId);
        productApi.updateProduct(productForm, clientId);

    }
}
