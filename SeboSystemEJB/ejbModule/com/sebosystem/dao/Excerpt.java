package com.sebosystem.dao;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;

@Entity
public class Excerpt implements Serializable {
	private static final long serialVersionUID = 2830862302998449076L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(updatable = false)
	private long oid;

	@Column(length = 3000, nullable = false)
	private String excerpt;

	@JoinColumn(nullable = false, updatable = false)
	private Book book;

	public long getOid() {
		return oid;
	}

	public void setOid(long oid) {
		this.oid = oid;
	}

	public String getExcerpt() {
		return excerpt;
	}

	public void setExcerpt(String excerpt) {
		this.excerpt = excerpt;
	}

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (oid ^ (oid >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Excerpt other = (Excerpt) obj;
		if (oid != other.oid)
			return false;
		return true;
	}

}
