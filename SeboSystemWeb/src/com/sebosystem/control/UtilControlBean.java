package com.sebosystem.control;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.NoneScoped;

import com.sebosystem.control.base.AbstractControlBean;
import com.sebosystem.dao.helper.EnumTypeKey;

@ManagedBean(name = "util")
@NoneScoped
public class UtilControlBean extends AbstractControlBean {

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

    public String returnMessage(String base, EnumTypeKey key) {
        return this.getLocalizedString(base.concat("_").concat(key.getKey()));
    }

}
