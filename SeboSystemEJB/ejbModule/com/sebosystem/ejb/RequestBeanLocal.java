package com.sebosystem.ejb;

import java.util.List;

import javax.ejb.Local;

import com.sebosystem.dao.Author;
import com.sebosystem.dao.Book;
import com.sebosystem.dao.Excerpt;
import com.sebosystem.dao.Request;
import com.sebosystem.dao.RequestType;
import com.sebosystem.dao.Review;
import com.sebosystem.dao.User;
import com.sebosystem.exception.SeboException;

@Local
public interface RequestBeanLocal {

    /**
     * Create a new {@link Request} about a riview's report (
     * {@link RequestType#ReviewReport})
     * 
     * @param review
     * @return
     * @throws SeboException
     */
    public Request newReviewReport(Review review) throws SeboException;

    /**
     * Create a new {@link Request} about a excerpt's report (
     * {@link RequestType#ExcerptReport})
     * 
     * @param excerpt
     * @return
     * @throws SeboException
     */
    public Request newExcerptReport(Excerpt excerpt) throws SeboException;

    /**
     * Create a new {@link Request} about a author duplicated issue (
     * {@link RequestType#AuthorDuplicated})
     * 
     * @param author
     * @return
     * @throws SeboException
     */
    public Request newAuthorDuplicated(Author author) throws SeboException;

    /**
     * Create a new {@link Request} about a book duplicated issue (
     * {@link RequestType#BookDuplicated})
     * 
     * @param author
     * @return
     * @throws SeboException
     */
    public Request newBookDuplicated(Book book) throws SeboException;

    /**
     * Persist or modify a {@link Request} passed by parameter
     * 
     * @param request
     * @return
     * @throws SeboException
     */
    public Request save(Request request) throws SeboException;

    /**
     * Associate the {@link Request} with the current {@link User}
     * 
     * @param request
     * @return
     * @throws SeboException 
     */
    public Request takeOn(Request request) throws SeboException;

    /**
     * Cancel the {@link Request}, remove it from the system and undo other
     * things
     * 
     * @param model
     * @throws SeboException
     */
    public void cancel(Request request) throws SeboException;

    /**
     * Reject the {@link Request}
     * 
     * @param model
     * @return
     * @throws Exception
     */
    public Request reject(Request model) throws Exception;

    /**
     * Accept the {@link Request}
     * 
     * @param model
     * @return
     * @throws Exception
     */
    public Request accept(Request model) throws Exception;

    /**
     * Retrive the {@link Request} with the {@code oid} of the paramenter
     * 
     * @param oid
     * @return
     * 
     * @see Request#getOid()
     */
    public Request getRequestByOid(long oid);

    public List<Request> getAllRequests();

    public List<Request> getOpenRequestsWithoutModerator();

    public List<Request> getOpenRequests();

    public List<Request> getRequestsByRequester(User requester);

    public List<Request> getOpenRequestsByRequester(User requester);

    public List<Request> getRequestsByModerator(User moderator);

    public List<Request> getOpenRequestsByModerator(User moderator);

    public List<Request> getRequestsWithoutModerator();

    public User getUserByOid(long oid);

    public void removeByReview(Review review);

    public void removeByExcerpt(Excerpt excerpt);

}
