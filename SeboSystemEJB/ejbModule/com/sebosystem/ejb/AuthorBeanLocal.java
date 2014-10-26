package com.sebosystem.ejb;

import java.util.List;

import javax.ejb.Local;

import com.sebosystem.dao.Author;
import com.sebosystem.dao.Book;
import com.sebosystem.exception.SeboException;

@Local
public interface AuthorBeanLocal {

    public Author save(Author author) throws Exception;

    public Author reportDuplicated(Author author) throws SeboException;

    public List<Author> getAllAuthors();

    public long getAuthorsTotalRows();

    public List<Author> getAllAuthors(int offset, int maxResults);

    public List<Author> getAuthorsByName(String name);

    public long getAuthorsByNameTotalRows(String name);

    public List<Author> getAuthorsByName(String name, int offset, int maxResults);

    public Author getAuthorByOid(long oid);

    public Author getAuthorByName(String name);

    public Author remove(Author author) throws Exception;

    public List<Book> getBooksOfAuthor(Author model);
}
