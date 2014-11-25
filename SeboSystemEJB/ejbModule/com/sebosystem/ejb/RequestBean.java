package com.sebosystem.ejb;

import java.util.Collections;
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
     * 
     * @throws SeboException
     */
    @Override
    @RolesAllowed({ "reader" })
    public Request newReviewReport(Review review) throws SeboException {
        Request request = this.newRequest(RequestType.ReviewReport);
        request.setReview(review);
        return this.save(request);
    }

    /**
     * {@inheritDoc}
     * 
     * @throws SeboException
     */
    @Override
    @RolesAllowed({ "reader" })
    public Request newExcerptReport(Excerpt excerpt) throws SeboException {
        Request request = this.newRequest(RequestType.ExcerptReport);
        request.setExcerpt(excerpt);
        return this.save(request);
    }

    /**
     * {@inheritDoc}
     * 
     * @throws SeboException
     */
    @Override
    @RolesAllowed({ "reader" })
    public Request newAuthorDuplicated(Author author) throws SeboException {
        Request req = this.newRequest(RequestType.AuthorDuplicated);
        req.setAuthor(author);
        return this.save(req);
    }

    /**
     * {@inheritDoc}
     * 
     * @throws SeboException
     */
    @Override
    @RolesAllowed({ "reader" })
    public Request newBookDuplicated(Book book) throws SeboException {
        Request req = this.newRequest(RequestType.BookDuplicated);
        req.setBook(book);
        return this.save(req);
    }

    /**
     * Valid if the current user is the moderator
     * 
     * @param request
     * @throws SeboException
     * 
     * @see UserBeanLocal#getCurrentUser()
     * @see Request#getModerator()
     */
    protected void validModerator(Request request) throws SeboException {

        if (this.getCurrentUser() == null)
            throw new SeboException("user_not_authenticated");

        if (!this.getCurrentUser().equals(request.getModerator()))
            throw new SeboException("request_user_is_not_moderator");
    }

    /**
     * Valid if the current user is the requester
     * 
     * @param request
     * @throws SeboException
     * 
     * @see UserBeanLocal#getCurrentUser()
     * @see Request#getRequester()
     */
    protected void validRequester(Request request) throws SeboException {

        if (this.getCurrentUser() == null)
            throw new SeboException("user_not_authenticated");

        if (!this.getCurrentUser().equals(request.getRequester()))
            throw new SeboException("request_user_is_not_requester");
    }

    /**
     * Valid if the current user is the moderator or requester
     * 
     * @param request
     * @throws SeboException
     * 
     * @see UserBeanLocal#getCurrentUser()
     * @see Request#getModerator()
     * @see Request#getRequester()
     */
    protected void validRequesterOrModerator(Request request) throws SeboException {

        if (this.getCurrentUser() == null)
            throw new SeboException("user_not_authenticated");

        if (!this.getCurrentUser().equals(request.getRequester()) && (!this.getCurrentUser().equals(request.getModerator())))
            throw new SeboException("request_user_is_nor_moderator_requester");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RolesAllowed({ "moderator" })
    public Request takeOn(Request request) throws SeboException {

        Request old = this.getRequestByOid(request.getOid());

        if (old.getModerator() != null) {
            throw new SeboException("request_has_moderator");
        }

        User user = this.getCurrentUser();

        if (old.getRequester().equals(user))
            throw new SeboException("request_moderator_request_diff");

        old.setModerator(user);
        request.setModerator(user);

        this.em.merge(old); // save the moderator

        return old;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @RolesAllowed({ "reader" })
    public Request save(Request request) throws SeboException {

        Request oldRequest = null;

        if (request.getOid() != 0)
            oldRequest = this.getRequestByOid(request.getOid());

        if (oldRequest != null) {
            this.validRequesterOrModerator(oldRequest);

            if (oldRequest.isClosed())
                throw new SeboException("request_already_closed");

            // keep values that can't be updated equals
            request.setType(oldRequest.getType());
            request.setRequester(oldRequest.getRequester());

            request.setBook(oldRequest.getBook());
            request.setAuthor(oldRequest.getAuthor());

            request.setBookCorrection(oldRequest.getBookCorrection());
            request.setAuthorCorrection(oldRequest.getAuthorCorrection());

            request.setReview(oldRequest.getReview());
            request.setExcerpt(oldRequest.getExcerpt());
        } else {
            request.setRequester(this.getCurrentUser()); // set the current user for the request
        }

        if (request.getType() == null)
            throw new SeboException("request_type_required");

        request.clearFieldsByType();

        if (request.getRequester() == null)
            throw new SeboException("request_requester_required");

        if (request.getModerator() != null) {
            if (request.getModerator().equals(request.getRequester()))
                throw new SeboException("request_moderator_request_diff");
        }

        if (request.isBookDuplicated()) {
            this.saveBookDuplicated(request, oldRequest);
        }

        if (request.getRequestDate() == null)
            request.setRequestDate(new Date());

        request.setLastUpdate(new Date());

        if (this.getRequestByOid(request.getOid()) == null) {
            this.em.persist(request);
        } else {
            this.em.merge(request);
        }

        return request;
    }

    /**
     * Valid a {@link Request} of type {@link RequestType#BookDuplicated}
     * 
     * @param newRequest
     *            New state of the {@link Request}
     * @param oldRequest
     *            Old state of the {@link Request}
     * @throws SeboException
     */
    protected void saveBookDuplicated(Request newRequest, Request oldRequest) throws SeboException {

        if (!newRequest.isBookDuplicated())
            return;

        Book book = this.bookBean.getBookByOid(newRequest.getBook().getOid());
        newRequest.setBook(book);

        if (newRequest.getBook() == null)
            throw new SeboException("request_book_duplicated_required");

        if (oldRequest == null) {

            if (book.isMarkedAsDuplicated())
                throw new SeboException("request_book_duplicated_already_in_progress", newRequest.getBook().getTitle(), newRequest.getBook().getOid());

        } else {

            // verify if any book was removed from the request
            for (Book oldBook : oldRequest.getRelatedBooks()) {
                if (!newRequest.getRelatedBooks().contains(oldBook)) {
                    oldBook.setMarkedAsDuplicated(false);
                    this.bookBean.save(oldBook);
                }
            }

            // verify if any book was added to the request
            for (Book newBook : newRequest.getRelatedBooks()) {

                if (newBook.equals(book))
                    newRequest.getRelatedBooks().remove(newBook);
                else {
                    if (!oldRequest.getRelatedBooks().contains(newBook)) {
                        if (newBook.isMarkedAsDuplicated())
                            throw new SeboException("request_book_duplicated_already_in_progress", //
                                    newBook.getTitle(), newBook.getOid());

                        newBook.setMarkedAsDuplicated(true);
                        this.bookBean.save(newBook);
                    }
                }

            }
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @throws SeboException
     */
    @Override
    public Request accept(Request request) throws SeboException {

        this.validModerator(this.getRequestByOid(request.getOid()));

        this.save(request);

        request = this.getRequestByOid(request.getOid());

        if (request.isBookDuplicated()) {
            this.acceptBookDuplicated(request);
        }

        if (request != null) {
            this.remove(request);
        }

        return request;
    }

    /**
     * Confirm books to merge
     * 
     * @param request
     * @throws Exception
     */
    protected void acceptBookDuplicated(Request request) throws SeboException {

        try {
            this.bookBean.acceptBookDuplicated(request);
        } catch (Exception e) {
            throw new SeboException(e);
        }

    }

    /**
     * {@inheritDoc}
     * 
     * @throws SeboException
     */
    @Override
    @RolesAllowed({ "moderator" })
    public Request reject(Request request) throws SeboException {

        this.validModerator(request);

        if (request.isBookDuplicated())
            request = this.rejectBookDuplicated(request);

        request = this.remove(request);
        return request;
    }

    /**
     * Reject a book duplicated {@link Request}
     * 
     * @param request
     * @throws SeboException
     */
    protected Request rejectBookDuplicated(Request request) throws SeboException {

        if (!request.isBookDuplicated())
            return request;

        List<Book> relatedBooks = request.getRelatedBooks();

        request.setRelatedBooks(Collections.<Book> emptyList());

        Book book = this.bookBean.getBookByOid(request.getBook().getOid());

        try {
            this.save(request);

            book.setMarkedAsDuplicated(false);
            this.bookBean.save(book);

        } catch (Exception e) {
            request.setRelatedBooks(relatedBooks);

            if (e instanceof SeboException)
                throw (SeboException) e;
            else
                throw new SeboException(e);
        }

        return request;
    }

    /**
     * Remove a {@link Request}
     * 
     * @param request
     * @return
     */
    protected Request remove(Request request) {
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

    /**
     * Get the first {@link Request} where it is open and has the {@link Book}
     * of parameter
     * 
     * @param book
     * @param type
     * @return
     */
    protected Request getOpenRequestByBook(Book book, RequestType type) {
        Query q = this.em.createNamedQuery("getOpenRequestByBook");
        q.setParameter("type", type);
        q.setParameter("book", book);

        @SuppressWarnings("unchecked")
        List<Request> list = q.getResultList();

        if (list.size() == 0)
            return null;
        else
            return list.get(0);
    }

    /**
     * Get the first {@link Request} where it is open and has the {@link Author}
     * of parameter
     * 
     * @param author
     * @param type
     * @return
     */
    protected Request getOpenRequestByAuthor(Author author, RequestType type) {
        Query q = this.em.createNamedQuery("getOpenRequestByAuthor");
        q.setParameter("type", type);
        q.setParameter("author", author);

        @SuppressWarnings("unchecked")
        List<Request> list = q.getResultList();

        if (list.size() == 0)
            return null;
        else
            return list.get(0);
    }

}
