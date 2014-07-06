package com.sebosystem.ejb;

import java.util.List;

import javax.ejb.Local;

import com.sebosystem.dao.Author;

@Local
public interface AuthorBeanLocal {

    public Author save(Author author) throws Exception;

    public List<Author> getAllAuthors();

    public List<Author> getNumAuthors();

    public List<Author> getAllAuthors(int offset, int maxResults);

    public List<Author> getAuthorsByName(String name);

    public List<Author> getNumAuthorsByName(String name);

    public List<Author> getAuthorsByName(String name, int offset, int maxResults);

    public Author getAuthorByOid(long oid);

    public Author getAuthorByName(String name);

    public Author remove(Author author);
}
