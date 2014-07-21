package com.sebosystem.security;

import java.util.Set;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;

import com.sebosystem.dao.RoleType;
import com.sebosystem.dao.User;

public class UserAuthozirationInfo extends SimpleAuthorizationInfo implements AuthorizationInfo {

    private static final long serialVersionUID = 2766738827011669495L;
    private User user;

    public UserAuthozirationInfo(User user) {
        this.user = user;

        if (this.user == null)
            this.addRoles(RoleType.Guest.getParentsSet());
        else
            this.addRoles(this.user.getRole().getParentsSet());
    }

    @Override
    public Set<String> getRoles() {
        if (!SecurityUtils.getSubject().isAuthenticated()) {
            SecurityUtils.getSubject().logout();
            return RoleType.Guest.getParentsSet();
        }

        System.out.printf("Roles for %s: %s", this.user.getName(), this.roles);
        return super.getRoles();
    }

}
