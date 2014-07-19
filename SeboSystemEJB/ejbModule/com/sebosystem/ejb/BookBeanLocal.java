package com.sebosystem.ejb;

import javax.ejb.Local;

import com.sebosystem.dao.Book;

@Local
public interface BookBeanLocal {

    public Book save(Book book) throws Exception;

    public Book getBookByOid(long oid);

}
