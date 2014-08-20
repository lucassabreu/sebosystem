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

}
