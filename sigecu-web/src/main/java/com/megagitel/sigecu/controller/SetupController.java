/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.megagitel.sigecu.controller;

import com.megagitel.sigecu.database.SetupService;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

/**
 *
 * @author jorgemalla
 */
@Startup
@Singleton
@TransactionManagement(TransactionManagementType.BEAN)
public class SetupController implements Serializable {

    @EJB
    private SetupService setupService;
    @PostConstruct
    public void init() {

        try {
            this.setupService.init();
        } catch (Exception e) {
            throw e;
        }
    }

    public SetupService getSetupService() {
        return setupService;
    }

    public void setSetupService(SetupService setupService) {
        this.setupService = setupService;
    }

}
