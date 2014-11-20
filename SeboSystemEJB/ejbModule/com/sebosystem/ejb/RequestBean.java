package com.sebosystem.ejb;

import java.util.Date;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.sebosystem.dao.Author;
import com.sebosystem.dao.Book;
import com.sebosystem.dao.Excerpt;
import com.sebosystem.dao.Request;
import com.sebosystem.dao.RequestType;
import com.sebosystem.dao.Review;
import com.sebosystem.dao.User;
import com.sebosystem.exception.SeboException;

/**
 * Session Bean implementation class RequestBean
 */
@Stateless
@LocalBean
public class RequestBean implements RequestBeanLocal {

    // TODO implementar as telas para cada tipo de requisição
    // TODO implementar regras para as alterações, considerando o usuário logado

    @PersistenceContext(name = "sebodbcontext")
    protected EntityManager em;

    @EJB
    protected UserBeanLocal userBean;

    @EJB
    protected BookBeanLocal bookBean;

    public RequestBean() {
    }

    /**
     * Retrieve the current user authenticated in the system
     * 
     * @return
     * 
     * @see UserBeanLocal#getCurrentUser()
     */
    protected User getCurrentUser() {
        return this.userBean.getCurrentUser();
    }

    /**
     * Create a new <code>Request</code> with default values
     * 
     * @param type
     * @return
     */
    @RolesAllowed({ "reader" })
    protected Request newRequest(RequestType type) {
        Request request = new Request();
        request.setRequester(this.getCurrentUser());
        request.setRequestDate(new Date());
        request.setType(type);
        return request;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RolesAllowed({ "reader" })
    public Request newReviewReport(Review review) {
        Request request = this.newRequest(RequestType.ReviewReport);
        request.setReview(review);
        return this.save(request);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RolesAllowed({ "reader" })
    public Request newExcerptReport(Excerpt excerpt) {
        Request request = this.newRequest(RequestType.ExcerptReport);
        request.setExcerpt(excerpt);
        return this.save(request);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RolesAllowed({ "reader" })
    public Request newAuthorDuplicated(Author author) {
        Request req = this.newRequest(RequestType.AuthorDuplicated);
        req.setAuthor(author);
        return this.save(req);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RolesAllowed({ "reader" })
    public Request newBookDuplicated(Book book) {
        Request req = this.newRequest(RequestType.BookDuplicated);
        req.setBook(book);
        return this.save(req);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RolesAllowed({ "reader" })
    public Request save(Request request) {
        // TODO Implementar controles para validar request
        if (this.getRequestByOid(request.getOid()) == null) {
            this.em.persist(request);
        } else {
            this.em.merge(request);
        }

        return request;
    }

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
    @Override
    public Request remove(Request request) {
        request = this.getRequestByOid(request.getOid());

        if (request != null)
            this.em.remove(request);

        return request;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RolesAllowed({ "reader" })
    public void cancel(Request request) throws SeboException {
        if (request == null)
            throw new SeboException("request_invalid");

        request = this.getRequestByOid(request.getOid());

        // only the request can cancel the request
        if (!request.getRequester().equals(this.getCurrentUser()))
            throw new SeboException("request_cancel_user_not_allowed");

        if (request.isBookDuplicated()) {
            this.bookBean.cancelBookDuplicated(request);
        }

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
