package com.sebosystem.dao;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import com.sebosystem.dao.helper.Ratable;

@Entity
@NamedQueries({
        @NamedQuery(name = "getAllAuthors", query = "SELECT a FROM Author a ORDER BY a.name"),
        @NamedQuery(name = "getAuthorsTotalRows", query = "SELECT COUNT(a) FROM Author a"),
        @NamedQuery(name = "getAuthorsByName", query = "SELECT a FROM Author a WHERE a.name LIKE :name ORDER BY a.name"),
        @NamedQuery(name = "getAuthorByName", query = "SELECT a FROM Author a WHERE a.name = :name"),
        @NamedQuery(name = "getAuthorsByNameTotalRows", query = "SELECT COUNT(a) FROM Author a WHERE a.name LIKE :name"),
})
public class Author extends AbstractAuthor implements Serializable, Ratable {

    private static final long serialVersionUID = 5645128118892142781L;

    @Column(nullable = false)
    protected int sumRating;

    @Column(nullable = false)
    protected int reviews;

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

    @Override
    public int getRating() {
        if (this.getReviews() > 0)
            return this.getSumRating() / this.getReviews();
        else
            return 3;
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
}
