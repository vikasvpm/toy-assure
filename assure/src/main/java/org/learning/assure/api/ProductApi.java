package org.learning.assure.api;

import org.learning.assure.dao.ProductDao;
import org.learning.assure.pojo.ProductPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
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
}
