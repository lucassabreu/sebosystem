package com.sebosystem.ejb;

import javax.ejb.Local;

import com.sebosystem.dao.Author;

@Local
public interface AuthorBeanLocal {
	
	public Author save(Author author);
	
	public int alive();
	public void test();
}
