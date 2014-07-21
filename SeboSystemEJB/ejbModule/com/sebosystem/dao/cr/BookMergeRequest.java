package com.sebosystem.dao.cr;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;

import com.sebosystem.dao.Book;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
//@DiscriminatorColumn(name = "TYPE", discriminatorType = DiscriminatorType.STRING)
public class BookMergeRequest extends Request {

    private static final long serialVersionUID = -8393443774339623877L;

    @JoinColumn(updatable = false)
    private Book correct;

    @JoinColumn(updatable = false)
    private Book duplicated;

    @Override
    public RequestType getType() {
        return RequestType.BookMerge;
    }

    public Book getCorrect() {
        return correct;
    }

    public void setCorrect(Book correct) {
        this.correct = correct;
    }

    public Book getDuplicated() {
        return duplicated;
    }

    public void setDuplicated(Book duplicated) {
        this.duplicated = duplicated;
    }
}
