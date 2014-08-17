package com.sebosystem.ejb;

import java.util.List;

import javax.ejb.Local;

import com.sebosystem.dao.Book;
import com.sebosystem.dao.Review;
import com.sebosystem.dao.User;

@Local
public interface ReviewBeanLocal {

    public Review getReviewByOid(long oid);

    public Review save(Review book) throws Exception;

    public Review remove(Review excerpt);

    public List<Review> getReviewsOfBook(Book book);

    public List<Review> getReviewsOfUser(User usableUser);

    public User getUserByOid(long oid);

    public Review report(Review model) throws Exception;

}
