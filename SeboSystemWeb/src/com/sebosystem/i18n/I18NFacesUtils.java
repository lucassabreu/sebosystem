package com.sebosystem.i18n;

import java.text.MessageFormat;
import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 * Util class for string localization
 * 
 * @author Lucas dos Santos Abreu <lucas.s.abreu@gmail.com>
 */
public abstract class I18NFacesUtils {

    public static String getLocalizedString(String key) {
        ResourceBundle bundle = FacesContext.getCurrentInstance().getApplication().getResourceBundle(FacesContext.getCurrentInstance(), "messages");
        return bundle.getString(key);
    }

    public static String getLocalizedString(String key, Object... parameters) {
        String message = getLocalizedString(key);

        if (parameters.length > 0)
            message = MessageFormat.format(message, parameters);

        return MessageFormat.format(getLocalizedString(key), parameters);
    }

    public static FacesMessage getLocalizedFacesMessage(String key, Object... params) {
        return new FacesMessage(getLocalizedString(key, params));
    }

}
