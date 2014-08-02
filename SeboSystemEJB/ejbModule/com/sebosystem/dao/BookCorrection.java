package com.sebosystem.dao;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@NamedQueries({
        @NamedQuery(name = "getBookCorrectionByBook", query = "SELECT bc FROM BookCorrection bc WHERE bc.book = :book")
})
public class BookCorrection extends Book {
    private static final long serialVersionUID = -460716766544112040L;

    @JoinColumn(nullable = false)
    private Book book;

    @Column(nullable = false, updatable = false)
    @Temporal(TemporalType.DATE)
    private Date dateRequest;

    @Column(nullable = false)
    private boolean approved;

    public Date getDateRequest() {
        return dateRequest;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public void setDateRequest(Date dateRequest) {
        this.dateRequest = dateRequest;
    }

}
