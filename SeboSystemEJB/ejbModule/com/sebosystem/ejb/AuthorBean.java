package com.sebosystem.ejb;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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

	@Override
	public int alive() {
		return 1;
	}

	@Override
	@WebMethod
	public void test() {
		Author a = new Author("Eduardo Spor");

		this.save(a);
	}

}
