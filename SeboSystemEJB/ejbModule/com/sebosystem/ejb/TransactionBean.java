package com.sebosystem.ejb;

import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.sebosystem.dao.Book;
import com.sebosystem.dao.Transaction;
import com.sebosystem.dao.User;

@Stateless
@LocalBean
public class TransactionBean implements TransactionBeanLocal {

    // TODO implementar regras e tela de transação, iniciar, alteração, confirmar e cancelar

    @PersistenceContext(name = "sebodbcontext")
    protected EntityManager em;

    @EJB
    protected UserBeanLocal userBean;

    @EJB
    private BookBeanLocal bookBean;

    public TransactionBean() {
    }

    @Override
    public Transaction save(Transaction transaction) throws Exception {

        if (this.getTransactionByOid(transaction.getOid()) == null) {

            transaction.setCreation(new Date());
            transaction.setUser(this.getCurrentUser());

            this.em.persist(transaction);
        } else {
            this.em.merge(transaction);
        }

        return transaction;
    }

    @Override
    public Transaction remove(Transaction transaction) {
        this.em.remove(transaction);
        return transaction;
    }

    @Override
    public Transaction getTransactionByOid(long oid) {
        return this.em.find(Transaction.class, new Long(oid));
    }

    @Override
    public User getUserByOid(long oid) {
        return this.userBean.getUserByOid(oid);
    }

    @Override
    public Book getBookByOid(long oid) {
        return this.bookBean.getBookByOid(oid);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Transaction> getTransactionsByUser(User user) {
        if (user == null)
            throw new IllegalArgumentException("User must be informed !");

        Query q = this.em.createNamedQuery("getTransactionsByUser");
        q.setParameter("user", user);

        return q.getResultList();
    }

    public User getCurrentUser() {
        return this.userBean.getCurrentUser();
    }
}
