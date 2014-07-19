package com.sebosystem.dao.cr;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.sebosystem.dao.User;

@Entity
@NamedQueries({
        @NamedQuery(name = "getAllRequests", query = "SELECT r FROM Request r"),
})
public abstract class Request implements Serializable {

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

    public abstract RequestType getType();

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
}
