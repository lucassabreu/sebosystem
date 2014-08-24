package com.sebosystem.ejb;

import java.util.Date;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.sebosystem.dao.Excerpt;
import com.sebosystem.dao.Request;
import com.sebosystem.dao.RequestType;
import com.sebosystem.dao.Review;
import com.sebosystem.dao.User;

/**
 * Session Bean implementation class RequestBean
 */
@Stateless
@LocalBean
public class RequestBean implements RequestBeanLocal {

    @PersistenceContext(name = "sebodbcontext")
    protected EntityManager em;

    @Inject
    protected UserBeanLocal userBean;

    public RequestBean() {
    }

    @Override
    public Request save(Request request) {
        // TODO Implementar controles para validar request
        if (this.getRequestByOid(request.getOid()) == null) {
            this.em.persist(request);
        } else {
            this.em.merge(request);
        }

        return request;
    }

    @Override
    public Request accept(Request request) {
        // TODO Implementar controles para accept request

        request = this.getRequestByOid(request.getOid());

        if (request != null) {
            request.setClosed(true);
            this.save(request);
        }

        return request;
    }

    @Override
    public Request reject(Request request) {
        // TODO Implementar controles para reject request

        request = this.getRequestByOid(request.getOid());

        if (request != null) {
            request.setClosed(true);
            this.save(request);
        }

        return request;
    }

    @Override
    public Request remove(Request request) {
        request = this.getRequestByOid(request.getOid());

        if (request != null)
            this.em.remove(request);
        
        return request;
    }

    @Override
    public void cancel(Request request) {
        this.remove(request);
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

    @SuppressWarnings("unchecked")
    @Override
    public List<Request> getOpenRequestsWithoutModerator() {
        Query q = this.em.createNamedQuery("getOpenRequestsWithoutModerator");
        return q.getResultList();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Request> getRequestsWithoutModerator() {
        Query q = this.em.createNamedQuery("getRequestsWithoutModerator");
        return q.getResultList();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Request> getOpenRequests() {
        Query q = this.em.createNamedQuery("getOpenRequests");
        return q.getResultList();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Request> getRequestsByRequester(User requester) {
        Query q = this.em.createNamedQuery("getRequestsByRequester");
        q.setParameter("requester", requester);
        return q.getResultList();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Request> getOpenRequestsByRequester(User requester) {
        Query q = this.em.createNamedQuery("getOpenRequestsByRequester");
        q.setParameter("requester", requester);
        return q.getResultList();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Request> getRequestsByModerator(User moderator) {
        Query q = this.em.createNamedQuery("getRequestsByModerator");
        q.setParameter("moderator", moderator);
        return q.getResultList();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Request> getOpenRequestsByModerator(User moderator) {
        Query q = this.em.createNamedQuery("getOpenRequestsByModerator");
        q.setParameter("moderator", moderator);
        return q.getResultList();
    }

    @Override
    public User getUserByOid(long oid) {
        return this.userBean.getUserByOid(oid);
    }

    protected Request newRequest(RequestType type) {
        Request request = new Request();
        //request.setRequester(this.userBean.getCurrentUser());
        // TODO Fazer do jeito certo
        request.setRequester(this.userBean.getUserByOid(2));
        request.setRequestDate(new Date());
        request.setType(type);
        return request;
    }

    @Override
    public Request newReviewReport(Review review) {
        Request request = this.newRequest(RequestType.ReviewReport);
        request.setReview(review);
        return this.save(request);
    }

    @Override
    public Request newExcerptReport(Excerpt excerpt) {
        Request request = this.newRequest(RequestType.ExceptReport);
        request.setExcerpt(excerpt);
        return this.save(request);
    }

    @Override
    public void removeByReview(Review review) {
        Query q = this.em.createNamedQuery("removeByReview");
        q.setParameter("review", review);
        q.executeUpdate();
    }

    @Override
    public void removeByExcerpt(Excerpt excerpt) {
        Query q = this.em.createNamedQuery("removeByExcerpt");
        q.setParameter("excerpt", excerpt);
        q.executeUpdate();
    }

}
