package com.sebosystem.control.base;

import java.io.Serializable;
import java.security.Principal;
import java.text.MessageFormat;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import com.sebosystem.dao.User;
import com.sebosystem.ejb.UserBeanLocal;
import com.sebosystem.i18n.I18NFacesUtils;

public abstract class AbstractControlBean implements Serializable {

    private static final long serialVersionUID = 1015512032111499242L;

    protected static String ANONYMOUS_USER = "ANONYMOUS";
    protected static String BLANK = "";

    @EJB
    private UserBeanLocal userBean;

    /**
     * Retrive the current <code>FacesContext</code> related with the request
     * 
     * @return
     */
    protected FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }

    /**
     * Retrive true if the user is authenticated
     * 
     * @return
     */
    public boolean isAuthenticated() {
        return this.getPrincipalAsUser() != null;
    }

    /**
     * Retrive if the user has that role
     * 
     * @param role
     *            Role for the test
     * @return
     */
    public boolean hasRole(String role) {
        return this.getRequest().isUserInRole(role);
    }

    /**
     * Add a new message on the stack
     * 
     * @param indicator
     * @param severity
     * @param message
     *            Message to be rendered, could has params
     * @param params
     *            List of parameters on the message
     */
    protected void addFacesMessage(String indicator, Severity severity, String message, Object... params) {
        if (params.length > 0)
            message = MessageFormat.format(message, params);

        this.getFacesContext().addMessage(indicator, new FacesMessage(severity, message, BLANK));
    }

    /**
     * Retrive the <code>Principal</code> from the
     * <code>HttpServletRequest</code>
     * 
     * @see #getRequest()
     * @return
     */
    protected Principal getPrincipal() {
        return this.getRequest().getUserPrincipal();
    }

    /**
     * Returns the Servlet Request object
     * 
     * @see ExternalContext#getRequest()
     * @return
     */
    protected HttpServletRequest getRequest() {
        return (HttpServletRequest) this.getFacesContext().getExternalContext().getRequest();
    }

    /**
     * Retrive the <code>User</code> related with the <code>Principal</code>
     * 
     * @see AbstractControlBean#getPrincipal()
     * @return
     */
    public User getPrincipalAsUser() {
        return this.userBean.getCurrentUser();
    }

    /**
     * Retrieve the string related with the param's code
     * 
     * @param code
     *            Key for the search on properties file
     * @see MessageFormat
     * @return
     */
    protected String getLocalizedString(String code, Object... params) {
        return I18NFacesUtils.getLocalizedString(code, params);
    }

    /**
     * Retrieve the <code>FacesMessage</code> related with the param's code
     * 
     * @param code
     *            Key for the search on properties file
     * @return
     * @see FacesMessage
     * @see #getLocalizedString(String, Object...)
     */
    protected FacesMessage getLocalizedMessage(String code, Object... params) {
        return I18NFacesUtils.getLocalizedFacesMessage(code, params);
    }

}
