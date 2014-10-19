package com.sebosystem.dao;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(indexes = {
        @Index(name = "idx_user_role", columnList = "EMAIL, ROLE")
})
@NamedQueries({
        @NamedQuery(name = "getUsersRoles", query = "SELECT ur FROM UsersRole ur WHERE ur.email = :email"),
        @NamedQuery(name = "isUserHasRole", query = "SELECT COUNT(ur) FROM UsersRole ur WHERE ur.email = :email AND ur.role = :role"),
        @NamedQuery(name = "removeUsersRoles", query = "DELETE FROM UsersRole ur WHERE ur.email = :email"),
})
public class UsersRole implements Serializable {

    private static final long serialVersionUID = 9133594888380032166L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long oid;

    @Column(nullable = false, updatable = false)
    private String email;

    @Column(length = 15, nullable = false)
    @Enumerated(EnumType.STRING)
    private RoleType role;

    public long getOid() {
        return oid;
    }

    public void setOid(long oid) {
        this.oid = oid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public RoleType getRole() {
        return role;
    }

    public void setRole(RoleType role) {
        this.role = role;
    }

}
