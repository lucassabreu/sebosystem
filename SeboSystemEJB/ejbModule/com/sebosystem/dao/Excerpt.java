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

@Entity
@NamedQueries({
        @NamedQuery(name = "getExcerptsOfBook", query = "SELECT e FROM Excerpt e WHERE e.book = :book AND e.published = true"),
        @NamedQuery(name = "getExcerptsOfUser", query = "SELECT e FROM Excerpt e WHERE e.user = :user")

})
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

    @JoinColumn(nullable = false, updatable = false)
    private User user;

    @Column(nullable = false)
    private boolean published;

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
