package org.learning.assure.dao;

import org.learning.assure.pojo.BinPojo;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
@Transactional
public class BinDao {

    @PersistenceContext
    EntityManager entityManager;

    public static final String SELECT_ALL = "select b from BinPojo b";
    public static final String SELECT_BY_BINID = "select b from BinPojo b where binId=:binId";

    @Transactional(readOnly = true)
    public List<BinPojo> getAllBins() {
        Query query = entityManager.createQuery(SELECT_ALL);
        return query.getResultList();
    }

    @Transactional(readOnly = true)
    public BinPojo getBinById(Long id) {
        TypedQuery<BinPojo> query = entityManager.createQuery(SELECT_BY_BINID, BinPojo.class);
        query.setParameter("binId", id);
        return query.getResultList().stream().findFirst().orElse(null);
    }

    public void addBin(BinPojo binPojo) {
        entityManager.persist(binPojo);
    }
}
