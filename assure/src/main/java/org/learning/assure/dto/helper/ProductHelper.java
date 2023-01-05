package org.learning.assure.dto.helper;

import org.learning.assure.model.form.ProductForm;
import org.learning.assure.pojo.ProductPojo;

import java.util.ArrayList;
import java.util.List;

public class ProductHelper {
    public static ProductPojo convertProductFromToProductPojo(ProductForm productForm, Long clientId) {
        ProductPojo productPojo = new ProductPojo();
        productPojo.setName(productForm.getName());
        productPojo.setDescription(productForm.getDescription());
        productPojo.setClientId(clientId);
        productPojo.setClientSkuId(productForm.getClientSkuId());
        productPojo.setBrandId(productForm.getBrandId());
        return productPojo;
    }

    public static List<ProductPojo> convertListOfProductFormToListOfProductPojo(List<ProductForm> productFormList, Long clientId) {
        List<ProductPojo> productPojoList = new ArrayList<>();
        for(ProductForm productForm : productFormList) {
            productPojoList.add(convertProductFromToProductPojo(productForm, clientId));
        }
        return productPojoList;
    }

}
