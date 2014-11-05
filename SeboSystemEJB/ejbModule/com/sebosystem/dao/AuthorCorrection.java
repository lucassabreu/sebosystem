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
        @NamedQuery(name = "getAuthorCorrectionByAuthor", query = "SELECT ac FROM AuthorCorrection ac WHERE ac.author = :author AND ac.approved = true")
})
public class AuthorCorrection extends AbstractAuthor {

    private static final long serialVersionUID = -460716766544112040L;

    @JoinColumn(nullable = false)
    private Author author;

    @Column(nullable = false, updatable = false)
    @Temporal(TemporalType.DATE)
    private Date dateRequest;

    @Column(nullable = false)
    private boolean approved;

    public Date getDateRequest() {
        return dateRequest;
    }

    public void setDateRequest(Date dateRequest) {
        this.dateRequest = dateRequest;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

}
