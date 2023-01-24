package org.learning.assure.dao;

import org.learning.assure.pojo.InventoryPojo;
import org.learning.assure.pojo.UserPojo;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

@Repository
@Transactional
public class InventoryDao {

    @PersistenceContext
    EntityManager entityManager;

    public static final String SELECT_BY_GLOBALSKUID = "select i from InventoryPojo i where globalSkuId=:globalSkuId";

    @Transactional(readOnly = true)
    public InventoryPojo getByGlobalSkuId(Long globalSkuId) {
        TypedQuery<InventoryPojo> query = entityManager.createQuery(SELECT_BY_GLOBALSKUID, InventoryPojo.class);
        query.setParameter("globalSkuId", globalSkuId);
        return query.getResultList().stream().findFirst().orElse(null);
    }

    public void addInventory(InventoryPojo inventoryPojo) {
        entityManager.persist(inventoryPojo);

    }
}
