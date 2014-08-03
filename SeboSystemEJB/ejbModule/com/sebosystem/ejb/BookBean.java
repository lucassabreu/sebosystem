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

    public BookBean() {
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
    public Book remove(Book book) {
        this.em.remove(book);
        return book;
    }

    @Override
    public Book getBookByOid(long oid) {
        return this.em.find(Book.class, new Long(oid));
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
    public Copy getCopyByUserAndBook(User user, Book book) {
        return this.copyBean.getCopyByUserAndBook(user, book);
    }

    @Override
    public Copy addBookToUser(Book book, User user) throws Exception {
        return this.copyBean.addBookToUser(book, user);
    }

    @Override
    public Copy removeBookOfUser(Book book, User user) throws Exception {
        return this.copyBean.removeBookOfUser(book, user);
    }
}
