package com.sebosystem.ejb;

import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.sebosystem.dao.cr.Request;

/**
 * Session Bean implementation class RequestBean
 */
@Stateless
@LocalBean
public class RequestBean implements RequestBeanLocal {

    @PersistenceContext(name = "sebodbcontext")
    protected EntityManager em;

    public RequestBean() {
    }

    @Override
    public Request save(Request request) {

        if (this.getRequestByOid(request.getOid()) == null) {
            this.em.persist(request);
        } else {
            this.em.merge(request);
        }

        return request;
    }

    @Override
    public Request remove(Request request) {
        this.em.remove(request);
        return request;
    }

    @Override
    public Request getRequestByOid(long oid) {
        return this.em.find(Request.class, new Long(oid));
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Request> getAllRequests() {
        Query q = this.em.createNamedQuery("getAllRequests");
        return q.getResultList();
    }

}
