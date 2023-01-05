package org.learning.assure.dao;

import org.learning.assure.pojo.BinSkuPojo;
import org.learning.assure.pojo.ProductPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

@Repository
public class BinSkuDao {
    @PersistenceContext
    EntityManager entityManager;

    public static final String SELECT_BY_BINID_AND_GLOBALSKUID = "select b from BinSkuPojo b where binId=:binId and globalSkuId=:globalSkuId";
    public void addBinSku(BinSkuPojo binSkuPojo) {
        entityManager.persist(binSkuPojo);
    }

    public BinSkuPojo getByBinIdAndGlobalSkuId(Long binId, Long globalSkuId) {
        TypedQuery<BinSkuPojo> query = entityManager.createQuery(SELECT_BY_BINID_AND_GLOBALSKUID, BinSkuPojo.class);
        query.setParameter("binId", binId);
        query.setParameter("globalSkuId", globalSkuId);
        return query.getResultList().stream().findFirst().orElse(null);
    }
}
