package com.sebosystem.control;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.NoneScoped;
import javax.inject.Inject;

import org.apache.shiro.subject.Subject;

import com.sebosystem.dao.User;

@ManagedBean(name = "utilControlBean")
@NoneScoped
public class UtilControlBean {

    @Inject
    private Subject currentUser;

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
        if (this.currentUser.isAuthenticated())
            return (User) this.currentUser.getPrincipal();
        else
            return null;
    }
}
