package com.sebosystem.dao;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@NamedQueries({
        @NamedQuery(name = "getAllAuthors", query = "SELECT a FROM Author a ORDER BY a.name"),
        @NamedQuery(name = "getAuthorsTotalRows", query = "SELECT COUNT(a) FROM Author a"),
        @NamedQuery(name = "getAuthorsByName", query = "SELECT a FROM Author a WHERE a.name LIKE :name ORDER BY a.name"),
        @NamedQuery(name = "getAuthorByName", query = "SELECT a FROM Author a WHERE a.name = :name"),
        @NamedQuery(name = "getAuthorsByNameTotalRows", query = "SELECT COUNT(a) FROM Author a WHERE a.name LIKE :name"),
})
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Author implements Serializable, RatableInterface {
    private static final long serialVersionUID = 5645128118892142781L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private long oid;

    @Column(nullable = false, length = 100, unique = true)
    private String name;

    @Column(nullable = false)
    private int sumRating;

    @Column(nullable = false)
    private int reviews;

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date birthday;

    @Column(nullable = true, length = 3000)
    private String description;

    @Column(nullable = false)
    private boolean markedAsDuplicated;

    @Lob()
    @Column(nullable = true)
    private byte[] picture;

    public Author() {
    }

    public Author(String name, Date birthday, int sumRating, int reviews) {
        super();
        this.name = name;
        this.birthday = birthday;
        this.sumRating = sumRating;
        this.reviews = reviews;
    }

    public Author(String name) {
        super();
        this.name = name;
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
        Author other = (Author) obj;
        if (oid != other.oid)
            return false;
        return true;
    }

    public int getRating() {
        if (this.getReviews() > 0)
            return this.getSumRating() / this.getReviews();
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

    public long getOid() {
        return oid;
    }

    public void setOid(long oid) {
        this.oid = oid;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    public boolean isMarkedAsDuplicated() {
        return markedAsDuplicated;
    }

    public void setMarkedAsDuplicated(boolean markedAsDuplicated) {
        this.markedAsDuplicated = markedAsDuplicated;
    }
}
