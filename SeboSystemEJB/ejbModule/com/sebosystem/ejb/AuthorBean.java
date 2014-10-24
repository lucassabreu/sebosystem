package com.sebosystem.ejb;

import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.sebosystem.dao.Author;
import com.sebosystem.dao.Book;

@Stateless
@LocalBean
public class AuthorBean implements AuthorBeanLocal {

    @PersistenceContext(name = "sebodbcontext")
    protected EntityManager em;
    
    // TODO criar regra para somente permitir inclusão direta
    // TODO criar regra para, nas alterações, gerar uma requisição

    @Inject
    protected BookBeanLocal bookBean;

    public AuthorBean() {
    }

    @Override
    public Author save(Author author) throws Exception {
        /*
        if (!SecurityUtils.getSubject().isAuthenticated())
            throw new Exception("You must be logged on to use this function !");
        */
        if (author.getName() == null || author.getName().trim().isEmpty())
            throw new Exception("Name must be informed !");

        if (author.getName().length() < 3)
            throw new Exception("Author's name must has more than 3 letters !");

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

    @Override
    public Author remove(Author author) throws Exception {
        author = this.getAuthorByOid(author.getOid());
        this.em.remove(author);
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
        q.setParameter("name", name.replace("%", "\\%").concat("%"));
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
        q.setParameter("name", name.replace("%", "\\%").concat("%"));
        Long count = (Long) q.getSingleResult();

        System.out.println("Count: " + count);

        if (count == null)
            return 0;

        return (Long) count;
    }

    @Override
    public List<Book> getBooksOfAuthor(Author author) {
        return this.bookBean.getBooksByAuthor(author);
    }

}
