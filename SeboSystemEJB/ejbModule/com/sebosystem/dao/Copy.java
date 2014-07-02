package com.sebosystem.dao;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;

@Entity
public class Copy implements Serializable {
	private static final long serialVersionUID = 1304403014426203162L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(updatable = false)
	private long oid;

	@Column(length = 1, nullable = false)
	private int rating;

	@Column(nullable = false)
	private boolean owned = false;

	@JoinColumn(nullable = false)
	private User user;

	@JoinColumn(nullable = false)
	private Book book;

	public Copy() {
	}

	public Copy(User user, Book book) {
		super();
		this.user = user;
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
		Copy other = (Copy) obj;
		if (oid != other.oid)
			return false;
		return true;
	}

	public long getOid() {
		return oid;
	}

	public void setOid(long oid) {
		this.oid = oid;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		if (this.rating == rating)
			return;

		this.book.setSumRating(this.book.getSumRating() - this.rating);

		if (this.rating != 0 && rating == 0) {
			this.book.setReviews(this.book.getReviews() - 1);
		} else {
			this.book.setSumRating(this.book.getSumRating() - this.rating);

			if (this.rating == 0)
				this.book.setReviews(this.book.getReviews() + 1);
		}

		this.rating = rating;
	}

	public boolean isOwned() {
		return owned;
	}

	public void setOwned(boolean owned) {
		this.owned = owned;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

}
