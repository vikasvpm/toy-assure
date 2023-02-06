package org.learning.assure.api;

import org.learning.assure.dao.ProductDao;
import org.learning.assure.pojo.ProductPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class ProductApi {

    @Autowired
    private ProductDao productDao;

    @Transactional(readOnly = true)
    public ProductPojo getProductByGlobalSkuId(Long globalSkuId) {
        return productDao.getProductByGlobalSkuId(globalSkuId);
    }

    @Transactional(readOnly = true)
    public List<ProductPojo> getAllProducts() {
        return productDao.getAllProducts();
    }

    public ProductPojo addProduct(ProductPojo productPojo) {
        ProductPojo exists = productDao.getProductByClientIdAndClientSkuId(productPojo.getClientId(),productPojo.getClientSkuId());
        if(Objects.isNull(exists)) {
            return productDao.addProduct(productPojo);
        }
        else {
            exists.setMrp(productPojo.getMrp());
            exists.setName(productPojo.getName());
            exists.setBrandId(productPojo.getBrandId());
            exists.setDescription(productPojo.getDescription());
            return exists;
        }
    }

    public List<ProductPojo> addProducts(List<ProductPojo> productPojoList) {
        List<ProductPojo> productPojoList1 = new ArrayList<>();
        for(ProductPojo productPojo : productPojoList) {
            productPojoList1.add(addProduct(productPojo));
        }
        return productPojoList1;
    }

    @Transactional(readOnly = true)
    public ProductPojo getProductByClientIdAndClientSkuId(Long clientId, String clientSkuId) {
            return productDao.getProductByClientIdAndClientSkuId(clientId, clientSkuId);
    }
}
