package com.sebosystem.control;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;

import com.sebosystem.dao.User;
import com.sebosystem.ejb.UserBeanLocal;

@ManagedBean(name = "UserSessionBean", eager = true)
@SessionScoped
public class UserSessionBean implements Serializable {

    private static final long serialVersionUID = -8124596995571776539L;
    private static final Logger logger = Logger.getLogger(UserSessionBean.class.getName());

    @Inject
    protected UserBeanLocal userBean;

    protected User loggedUser = null;

    private String username;
    private String password;
    private boolean rememberMe = false;

    private String locale;

    private static Map<String, Locale> countries;

    // authentication control
    public String authenticate() throws Exception {
        // Example using most common scenario of username/password pair:
        UsernamePasswordToken token = new UsernamePasswordToken(username, User.encriptPassword(password));

        // "Remember Me" built-in:
        token.setRememberMe(rememberMe);

        Subject currentUser = SecurityUtils.getSubject();

        logger.info("Submitting login with username of " + username);

        try {
            currentUser.login(token);
        } catch (AuthenticationException e) {
            // Could catch a subclass of AuthenticationException if you like
            logger.warning(e.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Username or password are invalid !"));
            return null;
        }

        return "pretty:user_profile";
    }

    public List<User> getAllUsers() {
        return this.userBean.getAllUsers();
    }

    // I18N controls...
    static {
        countries = new LinkedHashMap<String, Locale>();
        countries.put("English", Locale.ENGLISH);
        countries.put("French", Locale.FRENCH);
    }

    // value change event listener
    public void localeChanged(ValueChangeEvent e) {
        String newLocaleValue = e.getNewValue().toString();
        for (Map.Entry<String, Locale> entry : countries.entrySet()) {
            if (entry.getValue().toString().equals(newLocaleValue)) {
                FacesContext.getCurrentInstance().getViewRoot().setLocale((Locale) entry.getValue());
            }
        }
    }

    // getters and setters without "magic"

    public Map<String, Locale> getCountries() {
        return countries;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isRememberMe() {
        return rememberMe;
    }

    public void setRememberMe(boolean rememberMe) {
        this.rememberMe = rememberMe;
    }

    public User getLoggedUser() {
        return (User) SecurityUtils.getSubject().getPrincipal();
    }
}