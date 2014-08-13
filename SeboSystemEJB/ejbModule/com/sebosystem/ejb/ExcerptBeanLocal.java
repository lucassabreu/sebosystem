package com.sebosystem.ejb;

import java.util.List;

import javax.ejb.Local;

import com.sebosystem.dao.Book;
import com.sebosystem.dao.Excerpt;

@Local
public interface ExcerptBeanLocal {

    public Excerpt getExcerptByOid(long oid);

    public Excerpt save(Excerpt book) throws Exception;

    public Excerpt remove(Excerpt excerpt);

    public List<Excerpt> getExcerptsOfBook(Book book);

}
