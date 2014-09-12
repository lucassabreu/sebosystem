package com.sebosystem.ejb;

import java.util.List;

import javax.ejb.Local;

import com.sebosystem.dao.Book;
import com.sebosystem.dao.Transaction;
import com.sebosystem.dao.User;

@Local
public interface TransactionBeanLocal {

    public Transaction getTransactionByOid(long oid);

    public Transaction remove(Transaction transaction);

    public Transaction save(Transaction transaction) throws Exception;

    public List<Transaction> getTransactionsByUser(User user);

    public User getUserByOid(long oid);

    public Book getBookByOid(long oid);

}
