package com.sebosystem.control;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.NoneScoped;

import com.sebosystem.control.base.AbstractControlBean;
import com.sebosystem.dao.helper.EnumTypeKey;

@ManagedBean(name = "util")
@NoneScoped
public class UtilControlBean extends AbstractControlBean {

    private static final long serialVersionUID = -8414460510053008193L;

    /**
     * Convert all lines into paragrafhs
     * 
     * @param text
     * @return
     */
    public String formatText(String text) {
        return "<p>".concat(text.replace("\n", "</p><p>")).concat("</p>");
    }

    /**
     * Cut the string by the params
     * 
     * @param text
     * @param maxChars
     * @return
     */
    public String maxLength(String text, int maxChars) {

        if (text.length() > maxChars) {
            text = text.substring(0, maxChars - 3).concat("...");
        }

        return text;
    }

    /**
     * Retrives the localized string based on params
     * 
     * @param base
     * @param key
     * @return
     */
    public String returnMessage(String base, EnumTypeKey key) {
        if (key == null)
            return null;

        return this.getLocalizedString(base.concat("_").concat(key.getKey()));
    }

    /**
     * Return the position {@code index} of the array {@code values}
     * 
     * @param index
     * @param values
     * @return
     */
    public String choose(int index, String values) {
        return values.split(",")[index];
    }
}
