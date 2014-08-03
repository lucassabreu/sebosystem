package com.sebosystem.ejb;

import javax.ejb.Local;

import com.sebosystem.dao.Book;
import com.sebosystem.dao.Copy;
import com.sebosystem.dao.User;

@Local
public interface CopyBeanLocal {

    public Copy getCopyByUserAndBook(User user, Book book);

    public Copy addBookToUser(Book book, User user) throws Exception;

    public Copy removeBookOfUser(Book book, User user) throws Exception;

    public Copy getCopyByOid(long oid);

    public Copy remove(Copy copy);

    public Copy save(Copy copy) throws Exception;

}
