package com.sebosystem.ejb;

import java.util.List;

import javax.ejb.Local;

import com.sebosystem.dao.Author;

@Local
public interface AuthorBeanLocal {
	
	public Author save(Author author);
	public List<Author> getAllAuthors();
}
