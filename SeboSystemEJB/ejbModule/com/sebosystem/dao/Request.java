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
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@NamedQueries({
        @NamedQuery(name = "getAllRequests", query = "SELECT r FROM Request r"),
        @NamedQuery(name = "getAllOpenRequests", query = "SELECT r FROM Request r WHERE r.closed = false"),
        @NamedQuery(name = "getAllRequestsByRequester", query = "SELECT r FROM Request r WHERE r.requester = :requester"),
        @NamedQuery(name = "getAllRequestsByModerator", query = "SELECT r FROM Request r WHERE r.moderator = :moderator"),
        @NamedQuery(name = "getAllRequestsWithoutModerator", query = "SELECT r FROM Request r WHERE r.moderator IS NULL"),
})
@Table(indexes = {
        @Index(name = "idx_moderator", columnList = "MODERATOR_OID, CLOSED"),
        @Index(name = "idx_requester", columnList = "REQUESTER_OID, CLOSED"),
})
public class Request implements Serializable {

    private static final long serialVersionUID = -2751805054081390534L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private long oid;

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date requestDate;

    @JoinColumn(nullable = false)
    private User requester;

    @JoinColumn(nullable = true)
    private User moderator;

    @Column(length = 15, nullable = false, updatable = false)
    @Enumerated(EnumType.STRING)
    private RequestType type;

    @Column(nullable = false)
    private boolean closed;

    // Edit Book / Duplicated Book
    @JoinColumn(nullable = true, updatable = false)
    private Book book;

    // Edit Book (Book Correction)
    @JoinColumn(nullable = true, updatable = false)
    private BookCorrection bookCorrection;

    // Duplicated Book
    @OneToMany(cascade = { CascadeType.MERGE }, fetch = FetchType.LAZY)
    private List<Book> relatedBooks;

    // Edit Author / Duplicated Author
    @JoinColumn(nullable = true, updatable = false)
    private Author author;

    // Edit Author (Author Correction)
    @JoinColumn(nullable = true, updatable = false)
    private AuthorCorrection authorCorrection;

    // Duplicated Author
    @OneToMany(cascade = { CascadeType.MERGE }, fetch = FetchType.LAZY)
    private List<Author> relatedAuthors;

    // Reported Review
    @JoinColumn(nullable = true, updatable = false)
    private Review review;

    // Reported Excerpt
    @JoinColumn(nullable = true, updatable = false)
    private Excerpt excerpt;

    public void clearFieldsByType() {
        if (this.type == null)
            return;

        switch (this.type) {
            case AuthorDuplicated:
                this.setBook(null);
                this.setAuthorCorrection(null);
                this.setBookCorrection(null);
                this.setExcerpt(null);
                this.setReview(null);

                if (this.getRelatedBooks() != null)
                    this.getRelatedBooks().clear();
                break;
                
            case BookDuplicated:
                this.setAuthor(null);
                this.setAuthorCorrection(null);
                this.setBookCorrection(null);
                this.setExcerpt(null);
                this.setReview(null);

                if (this.getRelatedAuthors() != null)
                    this.getRelatedAuthors().clear();
                break;

            case AuthorRevision:
                this.setBook(null);
                this.setBookCorrection(null);
                this.setExcerpt(null);
                this.setReview(null);

                if (this.getRelatedBooks() != null)
                    this.getRelatedBooks().clear();

                if (this.getRelatedAuthors() != null)
                    this.getRelatedAuthors().clear();
                break;

            case BookRevision:
                this.setAuthor(null);
                this.setAuthorCorrection(null);
                this.setExcerpt(null);
                this.setReview(null);

                if (this.getRelatedBooks() != null)
                    this.getRelatedBooks().clear();

                if (this.getRelatedAuthors() != null)
                    this.getRelatedAuthors().clear();
                break;

            case ExceptReport:
                this.setBook(null);
                this.setBookCorrection(null);
                this.setAuthor(null);
                this.setAuthorCorrection(null);
                this.setReview(null);

                if (this.getRelatedBooks() != null)
                    this.getRelatedBooks().clear();

                if (this.getRelatedAuthors() != null)
                    this.getRelatedAuthors().clear();
                break;

            case ReviewReport:
                this.setBook(null);
                this.setBookCorrection(null);
                this.setAuthor(null);
                this.setAuthorCorrection(null);
                this.setExcerpt(null);

                if (this.getRelatedBooks() != null)
                    this.getRelatedBooks().clear();

                if (this.getRelatedAuthors() != null)
                    this.getRelatedAuthors().clear();
                break;
        }
    }

    public RequestType getType() {
        return this.type;
    }

    public void setType(RequestType type) {
        this.type = type;
    }

    public void setRequester(User requester) {
        this.requester = requester;
    }

    public User getRequester() {
        return requester;
    }

    public void setModerator(User moderator) {
        this.moderator = moderator;
    }

    public User getModerator() {
        return moderator;
    }

    public void setOid(long oid) {
        this.oid = oid;
    }

    public long getOid() {
        return oid;
    }

    public boolean isClosed() {
        return closed;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }

    public Date getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(Date requestDate) {
        this.requestDate = requestDate;
    }

    public List<Book> getRelatedBooks() {
        return relatedBooks;
    }

    public void setRelatedBooks(List<Book> relatedBooks) {
        this.relatedBooks = relatedBooks;
    }

    public List<Author> getRelatedAuthors() {
        return relatedAuthors;
    }

    public void setRelatedAuthors(List<Author> relatedAuthors) {
        this.relatedAuthors = relatedAuthors;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public Review getReview() {
        return review;
    }

    public void setReview(Review review) {
        this.review = review;
    }

    public Excerpt getExcerpt() {
        return excerpt;
    }

    public void setExcerpt(Excerpt excerpt) {
        this.excerpt = excerpt;
    }

    public BookCorrection getBookCorrection() {
        return bookCorrection;
    }

    public void setBookCorrection(BookCorrection bookCorrection) {
        this.bookCorrection = bookCorrection;
    }

    public AuthorCorrection getAuthorCorrection() {
        return authorCorrection;
    }

    public void setAuthorCorrection(AuthorCorrection authorCorrection) {
        this.authorCorrection = authorCorrection;
    }

}
