package com.sebosystem.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Transaction implements Serializable {

	private static final long serialVersionUID = 1717304226703141614L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(updatable = false)
	private int oid;

	@Column(nullable = false, updatable = false)
	private User user;

	@Column(nullable = true)
	private User interested;

	@Enumerated(EnumType.ORDINAL)
	@Column(length = 1)
	private TransactionStatus status;

	@Temporal(TemporalType.DATE)
	@Column(nullable = false, updatable = false)
	private Date creation;

	@Temporal(TemporalType.DATE)
	@Column(nullable = true)
	private Date confirmation;

	@Column(nullable = false)
	private int rating;

	@Column(nullable = false, length = 10, precision = 2)
	private float value;

	private List<BookInTransaction> books;

	public Transaction() {
	}

	public Transaction(User user) {
		super();
		this.user = user;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + oid;
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
		Transaction other = (Transaction) obj;
		if (oid != other.oid)
			return false;
		return true;
	}

	public int getOid() {
		return oid;
	}

	public void setOid(int oid) {
		this.oid = oid;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public User getInterested() {
		return interested;
	}

	public void setInterested(User interested) {
		this.interested = interested;
	}

	public TransactionStatus getStatus() {
		return status;
	}

	public void setStatus(TransactionStatus status) {
		this.status = status;
	}

	public Date getCreation() {
		return creation;
	}

	public void setCreation(Date creation) {
		this.creation = creation;
	}

	public Date getConfirmation() {
		return confirmation;
	}

	public void setConfirmation(Date confirmation) {
		this.confirmation = confirmation;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public float getValue() {
		return value;
	}

	public void setValue(float value) {
		this.value = value;
	}

	public List<BookInTransaction> getBooks() {
		if (this.books == null)
			this.books = new ArrayList<>();
		
		return this.books;
	}
}
