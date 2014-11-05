package com.sebosystem.dao;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import com.sebosystem.dao.helper.Ratable;

@Entity
@NamedQueries({
        @NamedQuery(name = "getAllBooks", query = "SELECT b FROM Book b"),
        @NamedQuery(name = "getBooksByAuthor", query = "SELECT b FROM Book b WHERE b.author = :author")
})
public class Book extends AbstractBook implements Serializable, Ratable {

    private static final long serialVersionUID = 3212854316405026625L;

    @Column(nullable = false)
    private int sumRating;

    @Column(nullable = false)
    private int reviews;

    public Book() {
    }

    public Book(Author author, String title, String description, int year, String edition) {
        super();
        this.author = author;
        this.title = title;
        this.description = description;
        this.year = year;
        this.edition = edition;
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
        Book other = (Book) obj;
        if (oid != other.oid)
            return false;
        return true;
    }

    @Override
    public int getRating() {
        if (this.reviews > 0)
            return this.sumRating / this.reviews;
        else
            return 0;
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
