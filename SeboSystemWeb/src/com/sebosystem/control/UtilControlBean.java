package com.sebosystem.control;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.NoneScoped;

import com.sebosystem.control.base.AbstractControlBean;
import com.sebosystem.dao.User;

@ManagedBean(name = "utilControlBean")
@NoneScoped
public class UtilControlBean extends AbstractControlBean {

    /*@Inject
    private Subject currentUser;*/

    private static final long serialVersionUID = -8414460510053008193L;

    public String formatText(String text) {
        return "<p>".concat(text.replace("\n", "</p><p>")).concat("</p>");
    }

    public String maxLength(String text, int maxChars) {

        if (text.length() > maxChars) {
            text = text.substring(0, maxChars - 3).concat("...");
        }

        return text;
    }

    public User getUser() {
        if (this.isAuthenticated())
            return this.getPrincipalAsUser();
        else
            return null;
    }
}
