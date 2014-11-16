package com.sebosystem.dao;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.sebosystem.dao.helper.Ratable;

@Entity
@NamedQueries({
        @NamedQuery(name = "getAllBooks", query = "SELECT b FROM Book b"),
        @NamedQuery(name = "getAllBooksCount", query = "SELECT COUNT(b) FROM Book b"),
        @NamedQuery(name = "getBooksByTitle", query = "SELECT b FROM Book b WHERE b.title LIKE :title"),
        @NamedQuery(name = "getBooksByTitleCount", query = "SELECT COUNT(b) FROM Book b WHERE b.title LIKE :title"),
        @NamedQuery(name = "getBooksByAuthor", query = "SELECT b FROM Book b WHERE b.author = :author"),
        @NamedQuery(name = "getBooksByAuthorCount", query = "SELECT COUNT(b) FROM Book b WHERE b.author = :author"),
        @NamedQuery(name = "getBooksByAuthorName", query = "SELECT b FROM Book b WHERE b.author.name LIKE :authorName"),
        @NamedQuery(name = "getBooksByAuthorNameCount", query = "SELECT COUNT(b) FROM Book b WHERE b.author.name LIKE :authorName"),
        @NamedQuery(name = "getBooksByYear", query = "SELECT b FROM Book b WHERE b.year = :year"),
        @NamedQuery(name = "getBooksByYearCount", query = "SELECT COUNT(b) FROM Book b WHERE b.year = :year"),
        @NamedQuery(name = "getBooksByExcerpt", query = "SELECT b FROM Book b WHERE "
                + "EXISTS(SELECT 1 FROM Excerpt e WHERE e.book = b AND e.excerpt LIKE :fragment)"),
        @NamedQuery(name = "getBooksByExcerptCount", query = "SELECT COUNT(b) FROM Book b WHERE "
                + "EXISTS(SELECT 1 FROM Excerpt e WHERE e.book = b AND e.excerpt LIKE :fragment)"),
        @NamedQuery(name = "getBooksByReview", query = "SELECT b FROM Book b WHERE "
                + "EXISTS(SELECT 1 FROM Review r WHERE r.book = b AND r.review LIKE :fragment)"),
        @NamedQuery(name = "getBooksByReviewCount", query = "SELECT COUNT(b) FROM Book b WHERE "
                + "EXISTS(SELECT 1 FROM Review r WHERE r.book = b AND r.review LIKE :fragment)"),
})
@Table(indexes = { @Index(name = "uniqueBook", unique = true, columnList = "AUTHOR_OID,TITLE") })
public class Book extends AbstractBook implements Serializable, Ratable {

    private static final long serialVersionUID = 3212854316405026625L;

    @Column(nullable = false)
    private int sumRating;

    @Column(nullable = false)
    private int reviews;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "book")
    private List<Excerpt> excerptList;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "book")
    private List<Review> reviewList;

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

    public List<Review> getReviewList() {
        return reviewList;
    }

    public void setReviewList(List<Review> reviewsList) {
        this.reviewList = reviewsList;
    }

    public List<Excerpt> getExcerptList() {
        return excerptList;
    }

    public void setExcerptList(List<Excerpt> excerptsList) {
        this.excerptList = excerptsList;
    }

}
