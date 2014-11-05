package com.sebosystem.dao;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.DatatypeConverter;

import com.sebosystem.dao.helper.Ratable;

@Entity
@NamedQueries({
        @NamedQuery(name = "authenticateUser", query = "SELECT u FROM User u WHERE u.email = :email AND u.encriptedPassword = :password"),
        @NamedQuery(name = "getAllUsers", query = "SELECT u FROM User u"),
        @NamedQuery(name = "getUserByEmail", query = "SELECT u FROM User u WHERE u.email = :email"),
})
@Table(name = "USER_TABLE")
public class User implements Serializable, Ratable {
    private static final long serialVersionUID = 3800255543775713159L;

    private static MessageDigest md;

    static {
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Id
    @Column(updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long oid;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, unique = true, length = 60)
    private String email;

    @Column(nullable = false, length = 45)
    private String encriptedPassword;

    @Column(nullable = false)
    private int sumRating;

    @Column(nullable = false)
    private int reviews;

    public User() {
    }

    public User(String name, String email, String password, int sumRating, int reviews) {
        super();
        this.name = name;
        this.email = email;
        this.encriptedPassword = password;
        this.sumRating = sumRating;
        this.reviews = reviews;
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
        User other = (User) obj;
        if (oid != other.oid)
            return false;
        return true;
    }

    public int getRating() {
        if (this.reviews > 0)
            return this.sumRating / this.reviews;
        else
            return 3;
    }

    public long getOid() {
        return oid;
    }

    public void setOid(long oid) {
        this.oid = oid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEncriptedPassword() {
        return encriptedPassword;
    }

    public void setPassword(String password) {
        if (password == null || password.isEmpty())
            this.encriptedPassword = "";

        this.setEncriptedPassword(User.encriptPassword(password));
    }

    public void setEncriptedPassword(String encriptedPassword) {
        this.encriptedPassword = encriptedPassword;
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

    public static String encriptPassword(String password) {
        if (password == null || password.length() == 0) {
            throw new IllegalArgumentException("String to encript cannot be null or zero length");
        }

        try {
            md.update(password.getBytes("UTF-8"));
            byte[] digest = md.digest();
            return DatatypeConverter.printBase64Binary(digest).toString();
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public String toString() {
        return String.format("%s (%s)", this.name, this.email);
    }

}
