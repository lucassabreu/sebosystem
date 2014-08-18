package com.sebosystem.ejb;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.sebosystem.dao.Request;
import com.sebosystem.dao.User;

/**
 * Session Bean implementation class RequestBean
 */
@Stateless
@LocalBean
public class RequestBean implements RequestBeanLocal {

    @PersistenceContext(name = "sebodbcontext")
    protected EntityManager em;

    @EJB
    protected UserBeanLocal userBean;

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

    @Override
    public User getUserByOid(long oid) {
        return this.userBean.getUserByOid(oid);
    }

}
