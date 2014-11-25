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
        @NamedQuery(name = "getAllRequests", query = "SELECT r FROM Request r ORDER BY r.requestDate"),

        @NamedQuery(name = "getRequestsByRequester", query = "SELECT r FROM Request r WHERE r.requester = :requester ORDER BY r.requestDate"),
        @NamedQuery(name = "getRequestsByModerator", query = "SELECT r FROM Request r WHERE r.moderator = :moderator ORDER BY r.requestDate"),
        @NamedQuery(name = "getRequestsWithoutModerator", query = "SELECT r FROM Request r WHERE r.moderator IS NULL ORDER BY r.requestDate"),

        @NamedQuery(name = "getOpenRequests", query = "SELECT r FROM Request r WHERE r.closed = false ORDER BY r.requestDate"),
        @NamedQuery(name = "getOpenRequestsByRequester", query = "SELECT r FROM Request r WHERE r.requester = :requester AND r.closed = false ORDER BY r.requestDate"),
        @NamedQuery(name = "getOpenRequestsByModerator", query = "SELECT r FROM Request r WHERE r.moderator = :moderator AND r.closed = false ORDER BY r.requestDate"),
        @NamedQuery(name = "getOpenRequestsWithoutModerator", query = "SELECT r FROM Request r WHERE r.moderator IS NULL AND r.closed = false ORDER BY r.requestDate"),

        @NamedQuery(name = "getOpenRequestByBook", query = "SELECT r FROM Request r WHERE r.type = :type AND r.closed = false AND r.book = :book"),
        @NamedQuery(name = "getOpenRequestByAuthor", query = "SELECT r FROM Request r WHERE r.type = :type AND r.closed = false AND r.author = :author"),

        @NamedQuery(name = "removeByReview", query = "DELETE FROM Request r WHERE r.review = :review AND r.review IS NOT NULL"),
        @NamedQuery(name = "removeByExcerpt", query = "DELETE FROM Request r WHERE r.excerpt = :excerpt AND r.excerpt IS NOT NULL"),

})
@Table(indexes = {
        @Index(name = "idx_moderator", columnList = "MODERATOR_OID, CLOSED"),
        @Index(name = "idx_requester", columnList = "REQUESTER_OID, CLOSED"),
})
/**
 * Class representation of a request in the system
 * 
 * @author Lucas dos Santos Abreu {@code lucas.s.abreu@gmail.com}
 * @author Fabrício Felisbino {@code fabricio.felisbino@outlook.com}
 */
public class Request implements Serializable {

    private static final long serialVersionUID = -2751805054081390534L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private long oid;

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date requestDate;

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date lastUpdate;

    @JoinColumn(nullable = false)
    private User requester;

    @JoinColumn(nullable = true)
    private User moderator;

    @Column(length = 20, nullable = false, updatable = false)
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

        if (this.isAuthorDuplicated()) {
            this.setBook(null);
            this.setAuthorCorrection(null);
            this.setBookCorrection(null);
            this.setExcerpt(null);
            this.setReview(null);

            if (this.getRelatedBooks() != null)
                this.getRelatedBooks().clear();
        }

        if (this.isBookDuplicated()) {
            this.setAuthor(null);
            this.setAuthorCorrection(null);
            this.setBookCorrection(null);
            this.setExcerpt(null);
            this.setReview(null);

            if (this.getRelatedAuthors() != null)
                this.getRelatedAuthors().clear();
        }

        if (this.isAuthorRevision()) {
            this.setBook(null);
            this.setBookCorrection(null);
            this.setExcerpt(null);
            this.setReview(null);

            if (this.getRelatedBooks() != null)
                this.getRelatedBooks().clear();

            if (this.getRelatedAuthors() != null)
                this.getRelatedAuthors().clear();
        }

        if (this.isBookRevision()) {
            this.setAuthor(null);
            this.setAuthorCorrection(null);
            this.setExcerpt(null);
            this.setReview(null);

            if (this.getRelatedBooks() != null)
                this.getRelatedBooks().clear();

            if (this.getRelatedAuthors() != null)
                this.getRelatedAuthors().clear();
        }

        if (this.isExceptReport()) {
            this.setBook(null);
            this.setBookCorrection(null);
            this.setAuthor(null);
            this.setAuthorCorrection(null);
            this.setReview(null);

            if (this.getRelatedBooks() != null)
                this.getRelatedBooks().clear();

            if (this.getRelatedAuthors() != null)
                this.getRelatedAuthors().clear();
        }

        if (this.isReviewReport()) {
            this.setBook(null);
            this.setBookCorrection(null);
            this.setAuthor(null);
            this.setAuthorCorrection(null);
            this.setExcerpt(null);

            if (this.getRelatedBooks() != null)
                this.getRelatedBooks().clear();

            if (this.getRelatedAuthors() != null)
                this.getRelatedAuthors().clear();
        }
    }

    public boolean isBookDuplicated() {
        return this.type == RequestType.BookDuplicated;
    }

    public boolean isAuthorDuplicated() {
        return this.type == RequestType.AuthorDuplicated;
    }

    public boolean isBookRevision() {
        return this.type == RequestType.BookRevision;
    }

    public boolean isAuthorRevision() {
        return this.type == RequestType.AuthorRevision;
    }

    public boolean isReviewReport() {
        return this.type == RequestType.ReviewReport;
    }

    public boolean isExceptReport() {
        return this.type == RequestType.ExcerptReport;
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

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    @Override
    public String toString() {
        return "Request [oid=" + oid + ", requester=" + requester.getName() + "]";
    }

}
