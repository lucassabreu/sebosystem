package com.sebosystem.dao.cr;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;

import com.sebosystem.dao.Author;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
//@DiscriminatorColumn(name = "TYPE", discriminatorType = DiscriminatorType.STRING)
public class AuthorMergeRequest extends Request {
    private static final long serialVersionUID = 3120735801850018185L;

    @JoinColumn(updatable = false)
    private Author correct;

    @JoinColumn(updatable = false)
    private Author duplicated;

    @Override
    public RequestType getType() {
        return RequestType.AuthorMerge;
    }

    public Author getCorrect() {
        return correct;
    }

    public void setCorrect(Author correct) {
        this.correct = correct;
    }

    public Author getDuplicated() {
        return duplicated;
    }

    public void setDuplicated(Author duplicated) {
        this.duplicated = duplicated;
    }
}
