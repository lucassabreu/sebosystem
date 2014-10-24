package com.sebosystem.ejb;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;
import javax.ejb.LocalBean;
import javax.ejb.SessionContext;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.sebosystem.dao.RoleType;
import com.sebosystem.dao.User;
import com.sebosystem.dao.UsersRole;

/**
 * Session Bean implementation class UserBean
 */
@Stateful
@LocalBean
public class UserBean implements UserBeanLocal, Serializable {

    // TODO melhorar tela de perfil
    // TODO implementar tela de configuração da conta

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

        User other = this.getUserByEmail(user.getEmail());

        if (other != null && other.getOid() != user.getOid())
            throw new Exception("Aready exists a user with that e-mail !");

        other = this.getUserByOid(user.getOid());

        if (other == null) {
            this.em.persist(user);
            this.setUsersRole(user, RoleType.Reader);
        } else {

            if (!other.getEmail().equalsIgnoreCase(user.getEmail())) {
                List<UsersRole> roles = this.getUsersRoles(other);

                for (UsersRole role : roles) {
                    role.setEmail(user.getEmail());
                    this.em.merge(role);
                }
            }

            this.em.merge(user);
        }

        return user;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<UsersRole> getUsersRoles(User user) {

        Query q = this.em.createNamedQuery("getUsersRoles");
        q.setParameter("email", user.getEmail());

        return q.getResultList();
    }

    protected void addRoleToUser(User user, RoleType role) {
        for (RoleType parentRole : role.getParents()) {
            this.addRoleToUser(user, parentRole);
        }

        if (!this.isUserHasRole(user, role)) {
            UsersRole ur = new UsersRole();
            ur.setEmail(user.getEmail());
            ur.setRole(role);
            this.em.persist(ur);
        }
    }

    @Override
    public void setUsersRole(User user, RoleType role) {
        this.removeRoles(user);
        this.addRoleToUser(user, role);
    }

    /**
     * Remove all the roles of the user
     * 
     * @param user
     */
    protected void removeRoles(User user) {
        Query q = this.em.createNamedQuery("removeUsersRoles");
        q.setParameter("email", user.getEmail());
        q.executeUpdate();
    }

    @Override
    public boolean isUserHasRole(User user, RoleType role) {

        Query q = this.em.createNamedQuery("isUserHasRole");
        q.setParameter("email", user.getEmail());
        q.setParameter("role", role);
        q.setMaxResults(1);

        return ((Long) q.getResultList().get(0)).longValue() > 0;
    }

    @SuppressWarnings("unchecked")
    @Override
    @RolesAllowed({ "reader" })
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

        if (email.isEmpty())
            return null;

        return this.getUserByEmail(email);
    }

}
