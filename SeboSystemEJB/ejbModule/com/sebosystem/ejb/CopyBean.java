package com.sebosystem.ejb;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.sebosystem.dao.Book;
import com.sebosystem.dao.Copy;
import com.sebosystem.dao.User;

@Stateless
@LocalBean
public class CopyBean implements CopyBeanLocal {

    @PersistenceContext(name = "sebodbcontext")
    protected EntityManager em;

    @EJB
    protected UserBeanLocal userBean;

    @EJB
    private BookBeanLocal bookBean;

    public CopyBean() {
    }

    @Override
    public Copy save(Copy copy) throws Exception {

        Copy other = this.getCopyByUserAndBook(copy.getUser(), copy.getBook());

        if (other != null && other.getOid() != copy.getOid())
            throw new Exception("Aready exists a Copy of this Book with the User");

        if (this.getCopyByOid(copy.getOid()) == null) {
            this.em.persist(copy);
        } else {
            this.em.merge(copy);
        }

        return copy;
    }

    @Override
    public Copy remove(Copy copy) {
        this.em.remove(copy);
        return copy;
    }

    @Override
    public Copy getCopyByOid(long oid) {
        return this.em.find(Copy.class, new Long(oid));
    }

    @Override
    public User getUserByOid(long oid) {
        return this.userBean.getUserByOid(oid);
    }

    @Override
    public Book getBookByOid(long oid) {
        return this.bookBean.getBookByOid(oid);
    }

    @Override
    public Copy addBookToUser(Book book, User user) throws Exception {
        Copy c = this.getCopyByUserAndBook(user, book);

        if (c != null) {
            if (!c.isOwned()) {
                c.setOwned(true);
                this.save(c);
            }
        } else {
            c = new Copy(user, book);
            c.setOwned(true);
            this.save(c);
        }

        return c;
    }

    @Override
    public Copy removeBookOfUser(Book book, User user) throws Exception {
        Copy c = this.getCopyByUserAndBook(user, book);

        // TODO Precisa ser implementado um controle para verificar as transações e confirmar se pode eliminar a copia do banco de dados, ou se apenas será marcada a flag de "possuida"

        if (c != null && c.isOwned()) {
            c.setOwned(false);
            this.save(c);
        }

        return c;
    }

    @Override
    public Copy getCopyByUserAndBook(User user, Book book) {

        if (user == null)
            throw new IllegalArgumentException("User must be informed !");

        if (book == null)
            throw new IllegalArgumentException("Book must be informed !");

        Query q = this.em.createNamedQuery("getCopyByUserAndBook");
        q.setParameter("user", user);
        q.setParameter("book", book);

        @SuppressWarnings("unchecked")
        List<Copy> copies = q.getResultList();

        if (copies.isEmpty())
            return null;
        else
            return copies.get(0);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Copy> getCopiesByUser(User user) {
        if (user == null)
            throw new IllegalArgumentException("User must be informed !");

        Query q = this.em.createNamedQuery("getCopiesByUser");
        q.setParameter("user", user);

        return q.getResultList();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Copy> getOwnedCopiesByUser(User user) {
        if (user == null)
            throw new IllegalArgumentException("User must be informed !");

        Query q = this.em.createNamedQuery("getOwnedCopiesByUser");
        q.setParameter("user", user);

        return q.getResultList();
    }
}
