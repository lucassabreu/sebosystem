package com.sebosystem.control;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import com.ocpsoft.pretty.faces.annotation.URLMapping;
import com.ocpsoft.pretty.faces.annotation.URLMappings;
import com.sebosystem.control.base.AbstractControlBean;
import com.sebosystem.dao.RoleType;
import com.sebosystem.dao.User;
import com.sebosystem.ejb.UserBeanLocal;
import com.sebosystem.i18n.I18NFacesUtils;

@ManagedBean(name = "sessionControlBean")
@SessionScoped
@URLMappings(mappings = {
        @URLMapping(id = "user_login", parentId = "index", viewId = "/faces/session/login.xhtml",
                pattern = "login"),
        @URLMapping(id = "user_logout", parentId = "index", viewId = "/faces/session/logout.xhtml",
                pattern = "logout"),

})
public class SessionControlBean extends AbstractControlBean {

    private static final Logger logger = Logger.getLogger(SessionControlBean.class.getName());
    private static final long serialVersionUID = 4042370291582472088L;

    protected static String READER_ROLE = RoleType.Reader.name().toLowerCase();
    protected static String MODERATOR_ROLE = RoleType.Moderator.name().toLowerCase();
    protected static String ADMIN_ROLE = RoleType.Administrator.name().toLowerCase();

    private String email;
    private String password;
    private boolean rememberMe = false;

    /* TODO remove this logic */
    @EJB
    protected UserBeanLocal userBean;
    private RoleType role;

    public RoleType getRole() {
        return role;
    }

    public void setRole(RoleType role) {
        this.role = role;
    }

    public String hasRoleAsString(String role) {
        return String.valueOf(this.hasRole(role));
    }

    public List<RoleType> getRoles() {
        return Arrays.asList(RoleType.values());
    }

    public String changeRole() {

        this.userBean.setUsersRole(this.getPrincipalAsUser(), this.role);

        return null;
    }

    /**
     * Realize login at security realm
     * 
     * @return
     */
    public String authenticate() {

        logger.info("Submitting login with username of " + this.email);
        // A regra não pode ser movida para o EJB, ele não possui controle sobre esse contexto

        try {
            this.getRequest().login(this.email, this.password);
            logger.info("User authenticated !");
        } catch (ServletException e) {
            // Could catch a subclass of AuthenticationException if you like
            logger.warning(e.getMessage());
            FacesContext.getCurrentInstance().addMessage("error", I18NFacesUtils.getLocalizedFacesMessage("login_error"));
            return null;
        }

        return "pretty:my_profile";
    }

    /**
     * Destroys session
     * 
     * @return
     */
    public String logout() {

        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();

        try {
            request.logout();
            request.getSession().invalidate();
        } catch (ServletException e) {
            e.printStackTrace();
        }

        return "pretty:index";
    }

    /**
     * Retrive if the user ahas "Reader" permission
     * 
     * @return
     */
    public boolean isHasReaderRole() {
        return this.hasRole(READER_ROLE);
    }

    /**
     * Retrive if the user ahas "Moderator" permission
     * 
     * @return
     */
    public boolean isHasModeratorRole() {
        return this.hasRole(MODERATOR_ROLE);
    }

    /**
     * Retrive if the user ahas "Administrator" permission
     * 
     * @return
     */
    public boolean isHasAdministratorRole() {
        return this.hasRole(ADMIN_ROLE);
    }

    /**
     * Retrieve the logged user, if it exists
     * 
     * @return
     */
    public User getUser() {
        if (isAuthenticated())
            return this.getPrincipalAsUser();
        else
            return null;
    }

    /**
     * Return if the session was authenticated
     */
    @Override
    public boolean isAuthenticated() {
        return super.isAuthenticated();
    }

    public boolean isNotAuthenticated() {
        return !this.isAuthenticated();
    }

    public boolean isUser(User user) {

        if (user == null || this.getUser() == null)
            return false;

        return user.getOid() == this.getUser().getOid();
    }

    public boolean isNotUser(User user) {
        return !this.isUser(user);
    }

    // getters and settings without magic

    public boolean isRememberMe() {
        return rememberMe;
    }

    public void setRememberMe(boolean rememberMe) {
        this.rememberMe = rememberMe;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
