/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.megagitel.sigecu.controller;

import com.megagitel.sigecu.util.SigecuController;
import com.ocpsoft.pretty.faces.annotation.URLMapping;
import com.ocpsoft.pretty.faces.annotation.URLMappings;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

/**
 *
 * @author jorgemalla
 */
@Named
@RequestScoped
@URLMappings(mappings = {
    @URLMapping(
            id = "inicio",
            pattern = "/home",
            viewId = "/faces/index.xhtml"
    )})
public class HomeController extends SigecuController implements Serializable {

    private Subject subject;

    @PostConstruct
    public void init() {
        subject = SecurityUtils.getSubject();
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

}
