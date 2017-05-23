/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.megagitel.sigecu.seguridad;

import com.megagitel.sigecu.seguridad.modelo.Usuario;
import java.io.Serializable;
import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.transaction.UserTransaction;

/**
 *
 * @author jorgemalla
 */
@Named
@RequestScoped
public class LoginController implements Serializable {

    @Resource
    private UserTransaction userTransaction;
    private Usuario usuario;

    public void autenticar() {

    }

    public Usuario getUsuario() {
        if (usuario == null) {
            usuario = new Usuario();
        }
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public UserTransaction getUserTransaction() {
        return userTransaction;
    }

    public void setUserTransaction(UserTransaction userTransaction) {
        this.userTransaction = userTransaction;
    }

}
