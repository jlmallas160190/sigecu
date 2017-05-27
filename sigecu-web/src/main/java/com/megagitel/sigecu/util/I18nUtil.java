/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.megagitel.sigecu.util;

import java.util.ResourceBundle;
import javax.faces.context.FacesContext;

/**
 *
 * @author jorgemalla
 */
public class I18nUtil {

    public static String getMessages(String key) {
        FacesContext fc = FacesContext.getCurrentInstance();
        ResourceBundle bundle = fc.getApplication().getResourceBundle(fc, "msg");
        return bundle.containsKey(key) ? bundle.getString(key) : key;
    }

}
