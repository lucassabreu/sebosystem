package com.sebosystem.ejb;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.LocalBean;
import javax.ejb.SessionContext;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.sebosystem.dao.RoleType;
import com.sebosystem.dao.User;

/**
 * Session Bean implementation class UserBean
 */
@Stateful
@LocalBean
public class UserBean implements UserBeanLocal, Serializable {

    private static final long serialVersionUID = -7103606389439556767L;

    @PersistenceContext(name = "sebodbcontext")
    protected EntityManager em;

    @Resource
    protected SessionContext session;

    public UserBean() {
    }

    @Override
    public User save(User user) throws Exception {

        if (user.getName() == null || user.getName().trim().isEmpty())
            throw new Exception("Name must be informed !");

        if (user.getEncriptedPassword() == null || user.getEncriptedPassword().trim().isEmpty())
            throw new Exception("Password must be informed !");

        if (user.getEmail() == null || user.getEmail().trim().isEmpty())
            throw new Exception("E-mail must be informed !");

        if (user.getRole() == null)
            user.setRole(RoleType.Reader);

        User other = this.getUserByEmail(user.getEmail());

        if (other != null && other.getOid() != user.getOid())
            throw new Exception("Aready exists a user with that e-mail !");

        if (this.getUserByOid(user.getOid()) == null) {
            this.em.persist(user);
        } else {
            this.em.merge(user);
        }

        return user;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<User> getAllUsers() {
        Query q = this.em.createNamedQuery("getAllUsers");
        return q.getResultList();
    }

    @Override
    public User getUserByOid(long oid) {
        return this.em.find(User.class, new Long(oid));
    }

    @Override
    public User getUserByEmail(String email) {

        if (email == null)
            throw new IllegalArgumentException("Email must be informed !");

        Query q = this.em.createNamedQuery("getUserByEmail");
        q.setParameter("email", email);

        @SuppressWarnings("unchecked")
        List<User> users = q.getResultList();

        if (users.isEmpty())
            return null;
        else
            return users.get(0);
    }

    @Override
    public User getCurrentUser() {
        String email = this.session.getCallerPrincipal().getName();

        System.out.println("Quem ? " + email);

        if (email.isEmpty())
            return null;

        return this.getUserByEmail(email);
    }

}
