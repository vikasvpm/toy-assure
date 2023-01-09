package org.learning.assure.dao;

import org.learning.assure.model.form.ProductForm;
import org.learning.assure.pojo.ProductPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class ProductDao {

    @PersistenceContext
    EntityManager entityManager;

    public static final String SELECT_ALL = "select p from ProductPojo p";
    public static final String SELECT_BY_GLOBALSKUID = "select p from ProductPojo p where globalSkuId=:globalSkuId";

    public static final String SELECT_BY_CLIENTID_AND_CLIENTSKUID = "select p from ProductPojo p where clientId=:clientId and clientSkuId=:clientSkuId";

    private static final String DELETE_BY_GLOBALSKUID = "delete from ProductPojo p where globalSkuId=:globalSkuId";

    private static final String SELECT_GLOBALSKUID = "select p.globalSkuId from ProductPojo p where clientId=:clientId and clientSkuId=:clientSkuId";


    public List<ProductPojo> getAllProducts() {
        Query query = entityManager.createQuery(SELECT_ALL);
        return query.getResultList();
    }

    public ProductPojo getProductByGlobalSkuId(Long globalSkuId) {
        TypedQuery<ProductPojo> query = entityManager.createQuery(SELECT_BY_GLOBALSKUID, ProductPojo.class);
        query.setParameter("globalSkuId", globalSkuId);
        return query.getResultList().stream().findFirst().orElse(null);
    }

    public void addProduct(ProductPojo productPojo) {
        entityManager.persist(productPojo);

    }

    public int deleteProductByGlobalSkuId(Long globalSkuId) {
        Query query = entityManager.createQuery(DELETE_BY_GLOBALSKUID);
        query.setParameter("globalSkuId", globalSkuId);
        return query.executeUpdate();
    }


    public void updateProduct(ProductForm productForm, Long clientId) {
        ProductPojo productPojo = getProductByClientIdAndClientSkuId(clientId, productForm.getClientSkuId());
        productPojo.setBrandId(productForm.getBrandId());
        productPojo.setName(productForm.getName());
        productPojo.setDescription(productForm.getDescription());
        productPojo.setMrp(productForm.getMrp());
    }

    public ProductPojo getProductByClientIdAndClientSkuId(Long clientId, String clientSkuId) {
        TypedQuery<ProductPojo> query = entityManager.createQuery(SELECT_BY_CLIENTID_AND_CLIENTSKUID, ProductPojo.class);
        query.setParameter("clientId", clientId);
        query.setParameter("clientSkuId", clientSkuId);
        return query.getResultList().stream().findFirst().orElse(null);
    }
}
