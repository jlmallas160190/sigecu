/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.megagitel.sigecu.util;

import com.megagitel.sigecu.seguridad.modelo.Log;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.hibernate.envers.RevisionListener;

/**
 *
 * @author jorgemalla
 */
public class LogListener implements RevisionListener {

    @Override
    public void newRevision(Object o) {
        try {
            Subject s = SecurityUtils.getSubject();
            Log log = (Log) o;
            log.setUsername(s.getPrincipal().toString());
        } catch (Exception e) {
            Log log = (Log) o;
        }

    }
}
