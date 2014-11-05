package com.sebosystem.dao;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.sebosystem.dao.helper.Ratable;

@Entity
@Table(name = "TRANSACTION_TABLE")
@NamedQueries({
        @NamedQuery(name = "getTransactionsByUser", query = "SELECT t FROM Transaction t WHERE t.user = :user OR t.interested = :user"),
})
public class Transaction implements Serializable, Ratable {

    private static final long serialVersionUID = 1717304226703141614L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private long oid;

    @JoinColumn(nullable = false, updatable = false)
    private User user;

    @JoinColumn(nullable = true)
    private User interested;

    @Enumerated(EnumType.STRING)
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

    @OneToMany(cascade = { CascadeType.PERSIST }, fetch = FetchType.LAZY)
    @JoinColumn(name = "TRANSACTION_OID", referencedColumnName = "OID", nullable = false)
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
        Transaction other = (Transaction) obj;
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
        return books;
    }

    public void setBooks(List<BookInTransaction> books) {
        this.books = books;
    }
}
