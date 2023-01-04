package org.learning.assure.dao;

import org.learning.assure.pojo.BinPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class BinDao {

    @PersistenceContext
    EntityManager entityManager;

    public static final String SELECT_ALL = "select b from BinPojo b";
    public static final String SELECT_BY_BINID = "select b from BinPojo b where binId=:binId";

    public List<BinPojo> getAllBins() {
        Query query = entityManager.createQuery(SELECT_ALL);
        return query.getResultList();
    }

    public BinPojo getBinById(Long id) {
        TypedQuery<BinPojo> query = entityManager.createQuery(SELECT_BY_BINID, BinPojo.class);
        query.setParameter("binId", id);
        return query.getSingleResult();
    }

    public void addBin(BinPojo binPojo) {
        entityManager.persist(binPojo);
    }
}
