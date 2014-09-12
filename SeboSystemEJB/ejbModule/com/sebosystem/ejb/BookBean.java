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

import com.sebosystem.dao.Author;
import com.sebosystem.dao.Book;
import com.sebosystem.dao.Copy;
import com.sebosystem.dao.Excerpt;
import com.sebosystem.dao.Review;
import com.sebosystem.dao.User;

/**
 * Session Bean implementation class BookBean
 */
@Stateless
@LocalBean
public class BookBean implements BookBeanLocal, Serializable {

    private static final long serialVersionUID = 7810629542685897342L;

    @PersistenceContext(name = "sebodbcontext")
    protected EntityManager em;

    @EJB
    protected AuthorBeanLocal authorBean;

    @EJB
    protected CopyBeanLocal copyBean;

    @EJB
    protected ReviewBeanLocal reviewBean;

    @EJB
    protected ExcerptBeanLocal excerptBean;

    @EJB
    protected UserBeanLocal userBean;

    public BookBean() {
    }

    @Override
    public Book getBookByOid(long oid) {
        return this.em.find(Book.class, new Long(oid));
    }

    @Override
    public Book save(Book book) throws Exception {
        if (book.getAuthor() == null)
            throw new Exception("Author must be informed !");

        if (book.getTitle() == null || book.getTitle().isEmpty())
            throw new Exception("Title must be informed !");

        if (book.getYear() == 0)
            throw new Exception("Year must be informed !");

        if (this.getBookByOid(book.getOid()) == null) {
            this.em.persist(book);
        } else {
            this.em.merge(book);
        }

        return book;
    }

    @Override
    public synchronized Book rateBook(Book book, User user, int rating) throws Exception {

        book = this.getBookByOid(book.getOid());
        user = this.userBean.getUserByOid(user.getOid());

        if (book == null || user == null)
            throw new Exception("User or Book does not exist!");

        Copy copy = this.getCopyByUserAndBook(user, book);

        if (copy == null) {
            copy = this.addBookToUser(book, user);
            copy.setOwned(false);
        }

        if (book.getReviews() == 0)
            book.getAuthor().setReviews(book.getAuthor().getReviews() + 1);

        if (copy.getRating() == 0)
            book.setReviews(book.getReviews() + 1);

        // remove the old rate of the user and add the new one
        int oldRating = book.getRating();
        book.setSumRating(book.getSumRating() - copy.getRating() + rating);
        book.getAuthor().setSumRating(book.getAuthor().getSumRating() - oldRating + book.getRating());
        copy.setRating(rating);

        this.save(book);
        this.copyBean.save(copy);
        this.authorBean.save(book.getAuthor());

        return book;
    }

    @Override
    public void restartRating() {
        try {
            for (Author author : this.authorBean.getAllAuthors()) {
                author.setReviews(0);
                author.setSumRating(0);
                this.authorBean.save(author);
            }

            for (Book book : this.getAllBooks()) {
                book.setSumRating(0);
                book.setReviews(0);
                this.save(book);
            }

            for (Copy copy : this.copyBean.getAllCopies()) {
                copy.setRating(0);
                this.copyBean.save(copy);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public Book remove(Book book) {
        this.em.remove(book);
        return book;
    }

    @Override
    public Copy addBookToUser(Book book, User user) throws Exception {
        return this.copyBean.addBookToUser(book, user);
    }

    @Override
    public Copy removeBookOfUser(Book book, User user) throws Exception {
        return this.copyBean.removeBookOfUser(book, user);
    }

    @Override
    public Copy getCopyByUserAndBook(User user, Book book) {
        return this.copyBean.getCopyByUserAndBook(user, book);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Book> getAllBooks() {
        Query q = this.em.createNamedQuery("getAllBooks");
        return q.getResultList();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Book> getBooksByAuthor(Author author) {

        author = this.authorBean.getAuthorByOid(author.getOid());

        if (author == null)
            return new ArrayList<Book>();

        Query q = this.em.createNamedQuery("getBooksByAuthor");
        q.setParameter("author", author);
        return q.getResultList();
    }

    @Override
    public List<Review> getReviewsOfBook(Book book) {
        return this.reviewBean.getReviewsOfBook(book);
    }

    @Override
    public List<Excerpt> getExcerptsOfBook(Book book) {
        return this.excerptBean.getExcerptsOfBook(book);
    }

    @Override
    public Review getReviewByOid(long oid) {
        return this.reviewBean.getReviewByOid(oid);
    }

    @Override
    public Excerpt getExcerptByOid(long oid) {
        return this.excerptBean.getExcerptByOid(oid);
    }
}
