package com.sebosystem.ejb;

import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.sebosystem.dao.Book;

/**
 * Session Bean implementation class BookBean
 */
@Stateless
@LocalBean
public class BookBean implements BookBeanLocal {

    @PersistenceContext(name = "sebodbcontext")
    protected EntityManager em;

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
}
