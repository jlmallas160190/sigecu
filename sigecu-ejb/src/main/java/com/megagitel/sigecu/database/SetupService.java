/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.megagitel.sigecu.database;

import com.megagitel.sigecu.core.modelo.Catalogo;
import com.megagitel.sigecu.core.modelo.CatalogoItem;
import com.megagitel.sigecu.seguridad.modelo.Usuario;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Sha256Hash;

/**
 *
 * @author jorgemalla
 */
@Stateless
public class SetupService implements Serializable {

    @PersistenceContext
    private EntityManager em;

    public void init() {
        Properties properties = new Properties();
        InputStream input = null;
        try {
            String filename = "config.properties";
            input = SetupService.class.getClassLoader().getResourceAsStream(filename);
            properties.load(input);
            this.getSuperUsuario();
            this.cargarCatalogos(properties.getProperty("rutaCatalogos"));
            this.cargarCatalogoItem(properties.getProperty("rutaCatalogosItems"));
        } catch (IOException e) {
            try {
                throw e;
            } catch (IOException ex) {
                Logger.getLogger(SetupService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void crearUsuario() {
        try {
            Usuario usuario = new Usuario();
            usuario.setNombre("administrador");
            RandomNumberGenerator rng = new SecureRandomNumberGenerator();
            Object salt = rng.nextBytes();
            String result = new Sha256Hash("admin", salt, 1024).toBase64();
            usuario.setClave(result);
            usuario.setEliminado(Boolean.FALSE);
            usuario.setSuperUsuario(Boolean.TRUE);
            getEm().persist(usuario);
        } catch (Exception e) {
            throw e;
        }

    }

    private Usuario getSuperUsuario() {
        Usuario singleResult = null;
        try {
            Query query = getEm().createQuery("SELECT u FROM Usuario u WHERE" + ""
                    + " (u.nombre='administrador')");
            singleResult = (Usuario) query.getSingleResult();
        } catch (NoResultException e) {
            this.crearUsuario();
        }
        return singleResult;
    }

    private void cargarCatalogos(String archivo) {
        BufferedReader br = null;
        String linea = "";
        String separador = ";";
        try {
            br = new BufferedReader(new FileReader(archivo));
            while ((linea = br.readLine()) != null) {
                String[] catalogo = linea.split(separador);
                getCatalogo(catalogo);
            }
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                }
            }
        }
    }

    private Catalogo getCatalogo(String[] catalogo) {
        Catalogo singleResult = null;
        try {
            Query query = getEm().createQuery("SELECT c FROM Catalogo c WHERE" + ""
                    + " (c.codigo=:codigo)");
            query.setParameter("codigo", catalogo[0]);
            singleResult = (Catalogo) query.getSingleResult();
        } catch (NoResultException e) {
            this.crearCatalogo(catalogo);
        }
        return singleResult;
    }

    private Catalogo buscarCatalogo(String codigo) {
        List<Catalogo> catalogos = new ArrayList<>();
        try {
            Query query = getEm().createQuery("SELECT c FROM Catalogo c WHERE" + ""
                    + " (c.codigo=:codigo)");
            query.setParameter("codigo", codigo);
            catalogos = query.getResultList();
        } catch (Exception e) {
            throw e;
        }
        return !catalogos.isEmpty() ? catalogos.get(0) : null;
    }

    private void crearCatalogo(String[] catalogo) {
        try {
            Catalogo catalog = new Catalogo();
            catalog.setCodigo(catalogo[0]);
            catalog.setNombre(catalogo[1]);
            catalog.setDescripcion(catalogo[1]);
            catalog.setEliminado(Boolean.FALSE);
            getEm().persist(catalog);
        } catch (Exception e) {
            throw e;
        }

    }

    private void cargarCatalogoItem(String archivo) {
        BufferedReader br = null;
        String linea = "";
        String separador = ";";
        try {
            br = new BufferedReader(new FileReader(archivo));
            while ((linea = br.readLine()) != null) {
                String[] catalogoItem = linea.split(separador);
                getCatalogoItem(catalogoItem);
            }
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                }
            }
        }
    }

    private CatalogoItem getCatalogoItem(String[] catalogoItemStr) {
        CatalogoItem singleResult = null;
        try {
            Query query = getEm().createQuery("SELECT c FROM CatalogoItem c WHERE" + ""
                    + " (c.codigo=:codigo)");
            query.setParameter("codigo", catalogoItemStr[0]);
            singleResult = (CatalogoItem) query.getSingleResult();
        } catch (NoResultException e) {
            this.crearCatalogoItem(catalogoItemStr);
        }
        return singleResult;
    }

    private void crearCatalogoItem(String[] catalogoItemStr) {
        try {
            Catalogo catalogo = this.buscarCatalogo(catalogoItemStr[2]);
            if (catalogo == null) {
                return;
            }
            CatalogoItem catalogoItem = new CatalogoItem();
            catalogoItem.setCodigo(catalogoItemStr[0]);
            catalogoItem.setNombre(catalogoItemStr[1]);
            catalogoItem.setDescripcion(catalogoItemStr[1]);
            catalogoItem.setEliminado(Boolean.FALSE);
            catalogoItem.setCatalogo(catalogo);
            getEm().persist(catalogoItem);
        } catch (Exception e) {
            throw e;
        }
    }

    public EntityManager getEm() {
        return em;
    }

    public void setEm(EntityManager em) {
        this.em = em;
    }

}
