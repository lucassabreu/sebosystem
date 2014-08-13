package com.sebosystem.ejb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.sebosystem.dao.Book;
import com.sebosystem.dao.Review;
import com.sebosystem.dao.User;

/**
 * Session Bean implementation class ReviewBean
 */
@Stateless
@LocalBean
public class ReviewBean implements ReviewBeanLocal, Serializable {

    private static final long serialVersionUID = 8954453635730956696L;

    @PersistenceContext(name = "sebodbcontext")
    protected EntityManager em;

    @EJB
    private BookBeanLocal bookBean;

    @EJB
    private UserBeanLocal userBean;

    public ReviewBean() {
    }

    @Override
    public Review getReviewByOid(long oid) {
        return this.em.find(Review.class, new Long(oid));
    }

    @Override
    public User getUserByOid(long oid) {
        return this.userBean.getUserByOid(oid);
    }

    @Override
    public Review save(Review review) throws Exception {
        if (review.getBook() == null)
            throw new Exception("Book must be informed !");

        if (review.getReview() == null || review.getReview().isEmpty())
            throw new Exception("Review must be informed !");

        if (this.getReviewByOid(review.getOid()) == null) {
            this.em.persist(review);
        } else {
            this.em.merge(review);
        }

        return review;
    }

    @Override
    public Review remove(Review review) {
        this.em.remove(review);
        return review;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Review> getReviewsOfBook(Book book) {

        book = this.bookBean.getBookByOid(book.getOid());

        if (book == null)
            return new ArrayList<Review>();

        Query q = this.em.createNamedQuery("getReviewsOfBook");
        q.setParameter("book", book);
        return q.getResultList();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Review> getReviewsOfUser(User user) {

        user = this.userBean.getUserByOid(user.getOid());

        if (user == null)
            return new ArrayList<Review>();

        Query q = this.em.createNamedQuery("getReviewsOfUser");
        q.setParameter("user", user);
        return q.getResultList();
    }
}
