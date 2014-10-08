package com.sebosystem.ejb;

import java.util.List;

import javax.ejb.Local;

import com.sebosystem.dao.User;

@Local
public interface UserBeanLocal {

    public User getCurrentUser();

    public User getUserByOid(long oid);

    public User save(User user) throws Exception;

    public List<User> getAllUsers();

    public User getUserByEmail(String email);

}
