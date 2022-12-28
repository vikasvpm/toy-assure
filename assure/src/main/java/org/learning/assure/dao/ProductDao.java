package org.learning.assure.dao;

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

    private static final String DELETE_BY_GLOBALSKUID = "delete from ProductPojo p where globalSkuId=:globalSkuId";


    public List<ProductPojo> getAllProducts() {
        Query query = entityManager.createQuery(SELECT_ALL);
        return query.getResultList();
    }

    public ProductPojo getProductByGlobalSkuId(Long globalSkuId) {
        TypedQuery<ProductPojo> query = entityManager.createQuery(SELECT_BY_GLOBALSKUID, ProductPojo.class);
        query.setParameter("globalSkuId", globalSkuId);
        return query.getSingleResult();
    }

    public void addProduct(ProductPojo productPojo) {
        entityManager.persist(productPojo);

    }

    public int deleteProductByGlobalSkuId(Long globalSkuId) {
        Query query = entityManager.createQuery(DELETE_BY_GLOBALSKUID);
        query.setParameter("globalSkuId", globalSkuId);
        return query.executeUpdate();
    }


}
