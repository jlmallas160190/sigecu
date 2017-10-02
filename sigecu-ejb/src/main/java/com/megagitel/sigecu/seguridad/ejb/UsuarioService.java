/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.megagitel.sigecu.seguridad.ejb;

import com.megagitel.sigecu.dao.AbstractDao;
import com.megagitel.sigecu.seguridad.modelo.Usuario;
import java.io.Serializable;
import javax.ejb.Stateless;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;

/**
 *
 * @author jorgemalla
 */
@Stateless
public class UsuarioService extends AbstractDao<Usuario> implements Serializable {

    public UsuarioService() {
        super(Usuario.class);
    }

    public Boolean autenticar(String username, String clave) {
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(username, clave);
        subject.login(usernamePasswordToken);
        return subject.isAuthenticated();
    }

    public void logout() {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
    }
}
