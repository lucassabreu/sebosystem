package com.sebosystem.ejb;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.sebosystem.dao.User;

/**
 * Session Bean implementation class UserBean
 */
@Named
@Stateless
@LocalBean
public class UserBean implements UserBeanLocal, Serializable {

    private static final long serialVersionUID = -7103606389439556767L;

    @PersistenceContext(name = "sebodbcontext")
    protected EntityManager em;

    private final static Logger logger = Logger.getLogger(UserBean.class.getName());

    public UserBean() {
    }

    @Override
    public User save(User user) throws Exception {

        if (user.getName().trim().isEmpty())
            throw new Exception("Name must be informed !");

        if (user.getPassword().trim().isEmpty())
            throw new Exception("Password must be informed !");

        if (user.getEmail().trim().isEmpty())
            throw new Exception("E-mail must be informed !");

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
    public User authenticate(String email, String password) {

        logger.info(String.format("E-mail: %s <> Password: %s", email, password));

        if (email == null || password == null)
            throw new IllegalArgumentException("Email and password must be informed !");

        Query q = this.em.createNamedQuery("authenticateUser");
        q.setParameter("email", email);
        q.setParameter("password", password);

        @SuppressWarnings("unchecked")
        List<User> users = q.getResultList();

        if (users.isEmpty())
            return null;
        else
            return users.get(0);
    }

}
