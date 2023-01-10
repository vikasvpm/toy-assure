package org.learning.assure.api;

import org.learning.assure.dao.ProductDao;
import org.learning.assure.model.form.ProductForm;
import org.learning.assure.pojo.ProductPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class ProductApi {

    @Autowired
    private ProductDao productDao;

    @Transactional
    public ProductPojo getProductByGlobalSkuId(Long globalSkuId) {
        return productDao.getProductByGlobalSkuId(globalSkuId);
    }

    @Transactional
    public List<ProductPojo> getAllProducts() {
        return productDao.getAllProducts();
    }

    @Transactional
    public void addProduct(ProductPojo productPojo) {
        productDao.addProduct(productPojo);
    }

    @Transactional
    public void deleteProduct(Long globalSkuId) {
        productDao.deleteProductByGlobalSkuId(globalSkuId);
    }

    @Transactional
    public void addProducts(List<ProductPojo> productPojoList) {
        for(ProductPojo productPojo : productPojoList) {
            productDao.addProduct(productPojo);
        }
    }

    @Transactional
    public void updateProduct(ProductForm productForm, Long clientId) {
            productDao.updateProduct(productForm, clientId);
    }

    @Transactional(readOnly = true)
    public ProductPojo getProductByClientIdAndClientSkuId(Long clientId, String clientSkuId) {
            return productDao.getProductByClientIdAndClientSkuId(clientId, clientSkuId);
    }
}
