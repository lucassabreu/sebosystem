package com.sebosystem.ejb;

import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.jws.WebService;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.sebosystem.dao.Author;

/**
 * Session Bean implementation class AuthorBean
 */
@Stateless
@LocalBean
@WebService
public class AuthorBean implements AuthorBeanLocal {

	@PersistenceContext(name = "sebodbcontext")
	protected EntityManager em;

	/**
	 * Default constructor.
	 */
	public AuthorBean() {
	}

	@Override
	public Author save(Author author) {

		if (author.getOid() != 0
				|| this.em.find(Author.class, author.getOid()) != null)
			this.em.persist(author);
		else
			this.em.merge(author);

		return author;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Author> getAllAuthors() {
		Query q = this.em.createNamedQuery("getAllAuthors");
		return q.getResultList();
	}

}
