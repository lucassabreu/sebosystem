package com.sebosystem.dao;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@NamedQueries({
        @NamedQuery(name = "getAllBooks", query = "SELECT b FROM Book b"),
        @NamedQuery(name = "getBooksByAuthor", query = "SELECT b FROM Book b WHERE b.author = :author")
})
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Book implements Serializable, RatableInterface {

    private static final long serialVersionUID = 3212854316405026625L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private long oid;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, length = 1000)
    private String description;

    @Column(nullable = false, length = 4)
    private int year;

    @Column(nullable = false)
    private String edition;

    @Column(nullable = false)
    private int sumRating;

    @Column(nullable = false)
    private int reviews;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Author author;

    @Column(nullable = false)
    private boolean markedAsDuplicated;

    @Lob()
    @Column(nullable = true)
    private byte[] cover;

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

    public int getRating() {
        if (this.reviews > 0)
            return this.sumRating / this.reviews;
        else
            return 0;
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

    public long getOid() {
        return oid;
    }

    public void setOid(long oid) {
        this.oid = oid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getEdition() {
        return edition;
    }

    public void setEdition(String edition) {
        this.edition = edition;
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

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public boolean isMarkedAsDuplicated() {
        return markedAsDuplicated;
    }

    public void setMarkedAsDuplicated(boolean markedAsDuplicated) {
        this.markedAsDuplicated = markedAsDuplicated;
    }

    public byte[] getCover() {
        return cover;
    }

    public void setCover(byte[] cover) {
        this.cover = cover;
    }

}
