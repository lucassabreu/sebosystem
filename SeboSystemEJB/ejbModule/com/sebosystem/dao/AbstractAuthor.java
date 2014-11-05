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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class AbstractAuthor implements Serializable {

    private static final long serialVersionUID = 4468617184324266068L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    protected long oid;

    @Column(nullable = false, length = 100, unique = true)
    protected String name;

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    protected Date birthday;

    @Column(nullable = true, length = 3000)
    protected String description;

    @Column(nullable = false)
    protected boolean markedAsDuplicated;

    @Lob()
    @Column(nullable = true)
    protected byte[] picture;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
