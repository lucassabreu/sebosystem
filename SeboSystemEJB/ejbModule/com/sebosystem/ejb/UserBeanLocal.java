package com.sebosystem.ejb;

import java.util.List;

import javax.ejb.Local;

import com.sebosystem.dao.RoleType;
import com.sebosystem.dao.User;
import com.sebosystem.dao.UsersRole;

@Local
public interface UserBeanLocal {

    public User getCurrentUser();

    public User getUserByOid(long oid);

    public User save(User user) throws Exception;

    public List<User> getAllUsers();

    public User getUserByEmail(String email);

    public boolean isUserHasRole(User user, RoleType role);

    public void setUsersRole(User user, RoleType role);

    public List<UsersRole> getUsersRoles(User user);

}
