package com.sebosystem.ejb;

import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.sebosystem.dao.Author;

/**
 * Session Bean implementation class AuthorBean
 */
@Stateless
@LocalBean
public class AuthorBean implements AuthorBeanLocal {

    @PersistenceContext(name = "sebodbcontext")
    protected EntityManager em;

    /**
     * Default constructor.
     */
    public AuthorBean() {
    }

    @Override
    public Author save(Author author) throws Exception {

        if (author.getName().trim().isEmpty())
            throw new Exception("Name must be informed !");

        if (author.getBirthday() == null)
            throw new Exception("Birthday must be informed !");

        Author other = this.getAuthorByName(author.getName());

        if (other != null && other.getOid() != author.getOid())
            throw new Exception("Already exists a Author with that name !");

        if (this.getAuthorByOid(author.getOid()) == null) {
            this.em.persist(author);
        } else {
            this.em.merge(author);
        }

        return author;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Author> getAllAuthors() {
        Query q = this.em.createNamedQuery("getAllAuthors");
        return q.getResultList();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Author> getAllAuthors(int offset, int maxResults) {
        Query q = this.em.createNamedQuery("getAllAuthors");
        q.setMaxResults(maxResults);
        q.setFirstResult(offset);
        return q.getResultList();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Author> getAuthorsByName(String name) {
        Query q = this.em.createNamedQuery("getAuthorsByName");
        q.setParameter("name", name.replace("%", "\\%") + "%");
        return q.getResultList();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Author> getAuthorsByName(String name, int offset, int maxResults) {
        Query q = this.em.createNamedQuery("getAuthorsByName");
        q.setParameter("name", name.replace("%", "\\%") + "%");
        q.setMaxResults(maxResults);
        q.setFirstResult(offset);
        return q.getResultList();
    }

    @Override
    public Author getAuthorByOid(long oid) {
        return this.em.find(Author.class, new Long(oid));
    }

    @Override
    public Author getAuthorByName(String name) {
        Query q = this.em.createNamedQuery("getAuthorByName");
        q.setParameter("name", name);

        @SuppressWarnings("unchecked")
        List<Author> authors = q.getResultList();

        if (authors.isEmpty())
            return null;

        return authors.get(0);
    }

    @Override
    public long getAuthorsTotalRows() {
        Query q = this.em.createNamedQuery("getAuthorsTotalRows");
        Long count = (Long) q.getSingleResult();

        if (count == null)
            return 0;

        return (Long) count;
    }

    @Override
    public long getAuthorsByNameTotalRows(String name) {
        Query q = this.em.createNamedQuery("getAuthorsByNameTotalRows");
        q.setParameter("name", name);
        Long count = (Long) q.getSingleResult();

        if (count == null)
            return 0;

        return (Long) count;
    }

    @Override
    public Author remove(Author author) {
        author = this.getAuthorByOid(author.getOid());
        this.em.remove(author);
        return author;
    }

}
