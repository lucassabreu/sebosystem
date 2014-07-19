package com.sebosystem.dao;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import sun.misc.BASE64Encoder;

@Entity
@NamedQueries({
        @NamedQuery(name = "authenticateUser", query = "SELECT u FROM User u WHERE u.email = :email AND u.encriptedPassword = :password"),
        @NamedQuery(name = "getAllUsers", query = "SELECT u FROM User u"),
        @NamedQuery(name = "getUserByEmail", query = "SELECT u FROM User u WHERE u.email = :email"),
})
public class User implements Serializable {
    private static final long serialVersionUID = 3800255543775713159L;

    private static MessageDigest messageDigest;
    private static BASE64Encoder encoder;

    @Id
    @Column(updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long oid;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, unique = true, length = 60)
    private String email;

    @Column(nullable = false, length = 32)
    private String encriptedPassword;

    @Column(nullable = false)
    private int sumRating;

    @Column(nullable = false)
    private int reviews;

    @Column(length = 10, nullable = false)
    @Enumerated(EnumType.STRING)
    private RoleType role;

    public User() {
    }

    public User(String name, String email, String password, int sumRating,
            int reviews, RoleType role) {
        super();
        this.name = name;
        this.email = email;
        this.encriptedPassword = password;
        this.sumRating = sumRating;
        this.reviews = reviews;
        this.role = role;
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

    public RoleType getRole() {
        return role;
    }

    public void setRole(RoleType role) {
        this.role = role;
    }

    public static String encriptPassword(String password) {

        if (messageDigest == null || encoder == null) {
            try {
                messageDigest = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }

            encoder = new BASE64Encoder();
        }

        password = encoder.encode(messageDigest.digest(password.getBytes()));

        return password;
    }

}
