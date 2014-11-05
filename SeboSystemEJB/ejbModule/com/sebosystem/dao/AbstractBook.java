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

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class AbstractBook implements Serializable {

    private static final long serialVersionUID = 6781480910809647198L;

    @Id
    @Column(updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected long oid;

    @Column(nullable = false, length = 100)
    protected String title;

    @Column(nullable = false, length = 1000)
    protected String description;

    @Column(nullable = false, length = 4)
    protected int year;

    @Column(nullable = false)
    protected String edition;
    @ManyToOne
    @JoinColumn(nullable = false)
    protected Author author;

    @Column(nullable = false)
    protected boolean markedAsDuplicated;

    @Lob()
    @Column(nullable = true)
    protected byte[] cover;

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
