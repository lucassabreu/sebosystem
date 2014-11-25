package com.sebosystem.dao;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/*
drop table AUTHOR, AUTHORCORRECTION, BOOK, BOOKCORRECTION, BOOKINTRANSACTION, COPY, EXCERPT, REQUEST, REQUEST_AUTHOR, REQUEST_BOOK, REVIEW, TRANSACTION_TABLE, USERSROLE, USER_TABLE, roles;            
*/

@Entity
@NamedQueries({
        @NamedQuery(name = "getReviewsOfBook", query = "SELECT r FROM Review r WHERE r.book = :book AND r.published = true"),
        @NamedQuery(name = "getReviewsOfUser", query = "SELECT r FROM Review r WHERE r.user = :user"),
        @NamedQuery(name = "updateBookReviewsFromTo", query = "UPDATE Review c SET c.book = :bookTo WHERE c.book = :bookFrom"),

})
public class Review implements Serializable {
    private static final long serialVersionUID = 2830862302998449076L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private long oid;

    @Column(length = 5000, nullable = false)
    private String review;

    @JoinColumn(nullable = false, referencedColumnName = "OID")
    private Book book;

    @JoinColumn()
    private User user;

    @Column(nullable = false)
    private boolean published;

    @Column(nullable = false)
    private boolean reported;

    public long getOid() {
        return oid;
    }

    public void setOid(long oid) {
        this.oid = oid;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }

    public boolean isReported() {
        return reported;
    }

    public void setReported(boolean reported) {
        this.reported = reported;
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
        Review other = (Review) obj;
        if (oid != other.oid)
            return false;
        return true;
    }

}
