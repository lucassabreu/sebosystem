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
import com.sebosystem.dao.Excerpt;
import com.sebosystem.dao.User;

/**
 * Session Bean implementation class ReviewBean
 */
@Stateless
@LocalBean
public class ExcerptBean implements ExcerptBeanLocal, Serializable {

    private static final long serialVersionUID = -1448599899915164595L;

    @PersistenceContext(name = "sebodbcontext")
    protected EntityManager em;

    @EJB
    private BookBeanLocal bookBean;

    @EJB
    private UserBeanLocal userBean;

    public ExcerptBean() {
    }

    @Override
    public Excerpt getExcerptByOid(long oid) {
        return this.em.find(Excerpt.class, new Long(oid));
    }

    @Override
    public Excerpt save(Excerpt book) throws Exception {
        if (book.getBook() == null)
            throw new Exception("Book must be informed !");

        if (book.getExcerpt() == null || book.getExcerpt().isEmpty())
            throw new Exception("Excerpt must be informed !");

        if (this.getExcerptByOid(book.getOid()) == null) {
            this.em.persist(book);
        } else {
            this.em.merge(book);
        }

        return book;
    }

    @Override
    public Excerpt report(Excerpt excerpt) throws Exception {

        excerpt = this.getExcerptByOid(excerpt.getOid());

        if (excerpt != null && !excerpt.isReported()) {
            excerpt.setReported(true);
            excerpt = this.save(excerpt);
        }

        return excerpt;
    }

    @Override
    public Excerpt remove(Excerpt excerpt) {

        excerpt = this.getExcerptByOid(excerpt.getOid());

        if (excerpt != null)
            this.em.remove(excerpt);

        return excerpt;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Excerpt> getExcerptsOfBook(Book book) {

        book = this.bookBean.getBookByOid(book.getOid());

        if (book == null)
            return new ArrayList<Excerpt>();

        Query q = this.em.createNamedQuery("getExcerptsOfBook");
        q.setParameter("book", book);
        return q.getResultList();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Excerpt> getExcerptsOfUser(User user) {

        user = this.getUserByOid(user.getOid());

        if (user == null)
            return new ArrayList<Excerpt>();

        Query q = this.em.createNamedQuery("getExcerptsOfUser");
        q.setParameter("user", user);
        return q.getResultList();
    }

    @Override
    public User getUserByOid(long oid) {
        return this.userBean.getUserByOid(oid);
    }

}
