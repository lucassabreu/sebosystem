package com.sebosystem.control;

import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import com.sebosystem.dao.Author;
import com.sebosystem.ejb.AuthorBeanLocal;

@ManagedBean(name = "AuthorRequestBean")
@RequestScoped
public class AuthorRequestBean {

	@EJB
	protected AuthorBeanLocal authorBean;

	protected Author model;

	public List<Author> getAllAuthors() {
		return this.authorBean.getAllAuthors();
	}

	public Author getModel() {
		return model;
	}

	public void setModel(Author model) {
		this.model = model;
	}
	
	public void search(){
		
	}
}
