package com.sebosystem.dao;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@NamedQueries({ @NamedQuery(name = "getAllAuthors", query = "SELECT a FROM Author a") })
public class Author implements Serializable {
	private static final long serialVersionUID = 5645128118892142781L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int oid;

	@Column(nullable = false, length = 100, unique = true)
	private String name;

	@Column(nullable = false)
	private int sumRating;

	@Column(nullable = false)
	private int reviews;

	public Author() {
	}

	public Author(String name, int sumRating, int reviews) {
		super();
		this.name = name;
		this.sumRating = sumRating;
		this.reviews = reviews;
	}

	public Author(String name) {
		super();
		this.name = name;
	}

	public int getRating() {
		if (this.reviews > 0)
			return this.sumRating / this.reviews;
		else
			return 3; 
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSumRating() {
		return sumRating;
	}

	public void setSumRating(int sumRating) {
		this.sumRating = sumRating;
	}

	public int getReviews() {
		return reviews;
	}

	public void setReviews(int reviews) {
		this.reviews = reviews;
	}

	public int getOid() {
		return oid;
	}

	public void setOid(int oid) {
		this.oid = oid;
	}
}
