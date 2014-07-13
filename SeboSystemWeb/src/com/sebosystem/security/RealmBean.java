package com.sebosystem.security;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.ops4j.pax.shiro.cdi.ShiroIni;

import com.sebosystem.dao.User;
import com.sebosystem.ejb.UserBeanLocal;

@ShiroIni
@Named("realmBean")
public class RealmBean extends AuthorizingRealm {

    @Inject
    @Named(value = "userBean")
    protected UserBeanLocal userBean;

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authToken) throws AuthenticationException {

        if (!(authToken instanceof UsernamePasswordToken))
            return null;

        UsernamePasswordToken upToken = (UsernamePasswordToken) authToken;

        String email = upToken.getUsername();
        String password = new String(upToken.getPassword());

        if (email == null || password == null)
            throw new AuthenticationException(String.format("E-mail and Password must be informed !"));

        User u = null;

        try {
            u = this.userBean.authenticate(email, password);
        } catch (Exception e) {
            e.printStackTrace();
            throw new AuthenticationException("Something went wrong...", e);
        }

        if (u == null)
            throw new AuthenticationException(String.format("User account %s does not exist !", email));

        return new SimpleAuthenticationInfo(u, u.getPassword(), getName());
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        User u = (User) principals.getPrimaryPrincipal();

        if (u != null)
            return new SimpleAuthorizationInfo(u.getRole().getParentsSet());
        else
            return new SimpleAuthorizationInfo();
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        if (token == null)
            return false;

        return (token instanceof UsernamePasswordToken);
    }

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }
}
