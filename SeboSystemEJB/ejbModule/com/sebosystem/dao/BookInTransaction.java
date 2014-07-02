package com.sebosystem.dao;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

@Entity
@Table(indexes = { @Index(name = "book_in_transaction", unique = true, columnList = "TRANSACTION_OID,BOOK_OID") })
public class BookInTransaction implements Serializable {
	private static final long serialVersionUID = -6702704948947035729L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(updatable = false)
	private long oid;

	@Column(length = 10, precision = 2)
	private float value;

	@JoinColumn(nullable = false, updatable = false, name = "BOOK_OID")
	private Book book;

	@JoinColumn(updatable = false)
	private User copyOwner;

	@JoinColumn(nullable = false, updatable = false, name = "TRANSACTION_OID")
	private Transaction transaction;

	public BookInTransaction() {
	}

	public BookInTransaction(Transaction transaction, User copyOwner,
			Book book, float value) {
		super();
		this.value = value;
		this.book = book;
		this.copyOwner = copyOwner;
		this.transaction = transaction;
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
		BookInTransaction other = (BookInTransaction) obj;
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

	public float getValue() {
		return value;
	}

	public void setValue(float value) {
		this.value = value;
	}

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	public User getCopyOwner() {
		return copyOwner;
	}

	public void setCopyOwner(User copyOwner) {
		this.copyOwner = copyOwner;
	}

	public Transaction getTransaction() {
		return transaction;
	}

	public void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}

	public boolean isFromOwner() {
		return this.copyOwner == this.transaction.getUser();
	}

	public boolean isFromInterested() {
		return !this.isFromOwner();
	}
}
