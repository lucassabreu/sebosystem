package com.sebosystem.ejb;

import java.util.List;

import javax.ejb.Local;

import com.sebosystem.dao.Excerpt;
import com.sebosystem.dao.Request;
import com.sebosystem.dao.Review;
import com.sebosystem.dao.User;

@Local
public interface RequestBeanLocal {

    public Request getRequestByOid(long oid);

    public Request save(Request request) throws Exception;

    public Request remove(Request request);

    public List<Request> getAllRequests();

    public User getUserByOid(long oid);

    public Request newReviewReport(Review review);

    public Request newExcerptReport(Excerpt excerpt);

    public void removeByReview(Review review);

    public void removeByExcerpt(Excerpt excerpt);

    public Request reject(Request model) throws Exception;

    public Request accept(Request model) throws Exception;

    public List<Request> getOpenRequestsWithoutModerator();

    public List<Request> getOpenRequests();

    public List<Request> getRequestsByRequester(User requester);

    public List<Request> getOpenRequestsByRequester(User requester);

    public List<Request> getRequestsByModerator(User moderator);

    public List<Request> getOpenRequestsByModerator(User moderator);

    public List<Request> getRequestsWithoutModerator();

    public void cancel(Request model);

}
