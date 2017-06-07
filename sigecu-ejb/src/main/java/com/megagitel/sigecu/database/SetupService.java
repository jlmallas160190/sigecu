/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.megagitel.sigecu.database;

import com.megagitel.sigecu.academico.modelo.ComponenteEducativo;
import com.megagitel.sigecu.academico.modelo.GrupoComponenteEducativo;
import com.megagitel.sigecu.core.modelo.Catalogo;
import com.megagitel.sigecu.core.modelo.CatalogoItem;
import com.megagitel.sigecu.core.modelo.DetalleParametrizacion;
import com.megagitel.sigecu.core.modelo.Institucion;
import com.megagitel.sigecu.core.modelo.Parametrizacion;
import com.megagitel.sigecu.enumeration.SigecuEnum;
import com.megagitel.sigecu.seguridad.modelo.GrupoUsuario;
import com.megagitel.sigecu.seguridad.modelo.Usuario;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
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
            this.getInstitucion();
            this.getParametrizacion();
            this.cargarCatalogos(properties.getProperty("rutaCatalogos"));
            this.cargarCatalogoItem(properties.getProperty("rutaCatalogosItems"));
            this.cargarGruposUsuario(properties.getProperty("rutaGruposUsuarios"));
            this.cargarDetallesParametrizacion(properties.getProperty("rutaDetallesParam"));
            this.cargarGruposComponenteEducativo(properties.getProperty("rutaGruposComponentesEducativos"));
            this.cargarComponenteEducativo(properties.getProperty("rutaComponentesEducativos"));
        } catch (IOException e) {
            try {
                throw e;
            } catch (IOException ex) {
                Logger.getLogger(SetupService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private Institucion getInstitucion() {
        Institucion singleResult = null;
        try {
            Query query = getEm().createQuery("SELECT u FROM Institucion u WHERE" + ""
                    + " (u.codigo='MEGAGITEL')");
            singleResult = (Institucion) query.getSingleResult();
        } catch (NoResultException e) {
            this.crearInstitucion();
        }
        return singleResult;
    }

    private void crearInstitucion() {
        try {
            Institucion institucion = new Institucion();
            institucion.setEmail("cursos@megagitel.com");
            institucion.setCodigo("MEGAGITEL");
            institucion.setNombre("Megagitel");
            institucion.setNumeroIdentificacion("9999999999");
            institucion.setActividadEconomica(this.buscarCatalogoItem(SigecuEnum.ACTIVIDAD_ECONOMICA_TELECOMUNICACIONES.getTipo()).getId());
            institucion.setTipoIdentificacion(this.buscarCatalogoItem(SigecuEnum.TIPO_DOCUMENTO_CEDULA.getTipo()).getId());
            getEm().persist(institucion);
        } catch (Exception e) {
            throw e;
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

    private void cargarGruposUsuario(String archivo) {
        BufferedReader br = null;
        String linea = "";
        String separador = ";";
        try {
            br = new BufferedReader(new FileReader(archivo));
            while ((linea = br.readLine()) != null) {
                String[] grupoUsuario = linea.split(separador);
                getGrupoUsuario(grupoUsuario);
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

    private GrupoUsuario getGrupoUsuario(String[] grupo) {
        GrupoUsuario singleResult = null;
        try {
            Query query = getEm().createQuery("SELECT g FROM GrupoUsuario g WHERE" + ""
                    + " (g.codigo=:codigo)");
            query.setParameter("codigo", grupo[0]);
            singleResult = (GrupoUsuario) query.getSingleResult();
        } catch (NoResultException e) {
            this.crearGrupoUsuario(grupo);
        }
        return singleResult;
    }

    private void crearGrupoUsuario(String[] grupo) {
        try {
            GrupoUsuario grupoUsuario = new GrupoUsuario();
            grupoUsuario.setCodigo(grupo[0]);
            grupoUsuario.setNombre(grupo[1]);
            grupoUsuario.setDescripcion(grupo[1]);
            grupoUsuario.setEliminado(Boolean.FALSE);
            grupoUsuario.setInstitucion(this.buscarInstitucion());
            getEm().persist(grupoUsuario);
        } catch (Exception e) {
            throw e;
        }
    }

    private void crearParametrizacion() {
        try {
            Parametrizacion parametrizacion = new Parametrizacion();
            parametrizacion.setCodigo("SIGECU");
            parametrizacion.setDescripcion("SIGECU");
            parametrizacion.setNombre("SIGECU");
            getEm().persist(parametrizacion);
        } catch (Exception e) {
        }
    }

    private Parametrizacion getParametrizacion() {
        Parametrizacion singleResult = null;
        try {
            Query query = getEm().createQuery("SELECT u FROM Parametrizacion u WHERE" + ""
                    + " (u.codigo='SIGECU')");
            singleResult = (Parametrizacion) query.getSingleResult();
        } catch (NoResultException e) {
            this.crearParametrizacion();
        }
        return singleResult;
    }

    private void cargarDetallesParametrizacion(String archivo) {
        BufferedReader br = null;
        String linea = "";
        String separador = ";";
        try {
            br = new BufferedReader(new FileReader(archivo));
            while ((linea = br.readLine()) != null) {
                String[] detalle = linea.split(separador);
                getDetalleParametrizacion(detalle);
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

    private DetalleParametrizacion getDetalleParametrizacion(String[] detalle) {
        DetalleParametrizacion singleResult = null;
        try {
            Query query = getEm().createQuery("SELECT c FROM DetalleParametrizacion c WHERE" + ""
                    + " (c.codigo=:codigo)");
            query.setParameter("codigo", detalle[0]);
            singleResult = (DetalleParametrizacion) query.getSingleResult();
        } catch (NoResultException e) {
            this.crearDetalleParametrizacion(detalle);
        }
        return singleResult;
    }

    private void crearDetalleParametrizacion(String[] detalle) {
        try {
            Parametrizacion parametrizacion = this.buscarParametrizacion(detalle[3]);
            if (parametrizacion == null) {
                return;
            }
            DetalleParametrizacion detalleParam = new DetalleParametrizacion();
            detalleParam.setCodigo(detalle[0]);
            detalleParam.setNombre(detalle[1]);
            detalleParam.setValor(detalle[2]);
            detalleParam.setDescripcion(detalle[1]);
            detalleParam.setParametrizacion(parametrizacion);
            getEm().persist(detalleParam);
        } catch (Exception e) {
            throw e;
        }
    }

    private Parametrizacion buscarParametrizacion(String codigo) {
        List<Parametrizacion> parametrizacions = new ArrayList<>();
        try {
            Query query = getEm().createQuery("SELECT c FROM Parametrizacion c WHERE" + ""
                    + " (c.codigo=:codigo)");
            query.setParameter("codigo", codigo);
            parametrizacions = query.getResultList();
        } catch (Exception e) {
            throw e;
        }
        return !parametrizacions.isEmpty() ? parametrizacions.get(0) : null;
    }

    private void cargarGruposComponenteEducativo(String archivo) {
        BufferedReader br = null;
        String linea = "";
        String separador = ";";
        try {
            br = new BufferedReader(new FileReader(archivo));
            while ((linea = br.readLine()) != null) {
                String[] grupoEducativo = linea.split(separador);
                getGrupoComponenteEducativo(grupoEducativo);
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

    private GrupoComponenteEducativo getGrupoComponenteEducativo(String[] grupoComponente) {
        GrupoComponenteEducativo singleResult = null;
        try {
            Query query = getEm().createQuery("SELECT c FROM GrupoComponenteEducativo c WHERE" + ""
                    + " (c.codigo=:codigo)");
            query.setParameter("codigo", grupoComponente[0]);
            singleResult = (GrupoComponenteEducativo) query.getSingleResult();
        } catch (NoResultException e) {
            this.crearGrupoComponenteEducativo(grupoComponente);
        }
        return singleResult;
    }

    private void crearGrupoComponenteEducativo(String[] grupoComponente) {
        try {
            GrupoComponenteEducativo grupo = new GrupoComponenteEducativo();
            grupo.setCodigo(grupoComponente[0]);
            grupo.setNombre(grupoComponente[1]);
            grupo.setEliminado(Boolean.FALSE);
            grupo.setDescripcion(grupoComponente[2]);
            getEm().persist(grupo);
        } catch (Exception e) {
            throw e;
        }
    }

    private void cargarComponenteEducativo(String archivo) {
        BufferedReader br = null;
        String linea = "";
        String separador = ";";
        try {
            br = new BufferedReader(new FileReader(archivo));
            while ((linea = br.readLine()) != null) {
                String[] compoenteEducativo = linea.split(separador);
                getComponenteEducativo(compoenteEducativo);
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

    private ComponenteEducativo getComponenteEducativo(String[] grupoComponente) {
        ComponenteEducativo singleResult = null;
        try {
            Query query = getEm().createQuery("SELECT c FROM ComponenteEducativo c WHERE" + ""
                    + " (c.codigo=:codigo)");
            query.setParameter("codigo", grupoComponente[0]);
            singleResult = (ComponenteEducativo) query.getSingleResult();
        } catch (NoResultException e) {
            this.crearComponenteEducativo(grupoComponente);
        }
        return singleResult;
    }

    private void crearComponenteEducativo(String[] componenteEducativo) {
        try {
            GrupoComponenteEducativo grupoComponenteEducativo = this.buscarGrupoComponenteEducativo(componenteEducativo[4]);
            if (grupoComponenteEducativo == null) {
                return;
            }
            ComponenteEducativo componente = new ComponenteEducativo();
            componente.setCodigo(componenteEducativo[0]);
            componente.setNombre(componenteEducativo[1]);
            componente.setCreditos(new BigDecimal(componenteEducativo[3]));
            componente.setDescripcion(componenteEducativo[2]);
            componente.setGrupoComponenteEducativo(grupoComponenteEducativo);
            componente.setEliminado(Boolean.FALSE);
            getEm().persist(componente);
        } catch (Exception e) {
            throw e;
        }
    }

    private GrupoComponenteEducativo buscarGrupoComponenteEducativo(String codigo) {
        List<GrupoComponenteEducativo> grupoComponenteEducativos = new ArrayList<>();
        try {
            Query query = getEm().createQuery("SELECT c FROM GrupoComponenteEducativo c WHERE" + ""
                    + " (c.codigo=:codigo)");
            query.setParameter("codigo", codigo);
            grupoComponenteEducativos = query.getResultList();
        } catch (Exception e) {
            throw e;
        }
        return !grupoComponenteEducativos.isEmpty() ? grupoComponenteEducativos.get(0) : null;
    }

    private CatalogoItem buscarCatalogoItem(String codigo) {
        CatalogoItem singleResult = null;
        try {
            Query query = getEm().createQuery("SELECT c FROM CatalogoItem c WHERE" + ""
                    + " (c.codigo=:codigo)");
            query.setParameter("codigo", codigo);
            singleResult = (CatalogoItem) query.getSingleResult();
        } catch (NoResultException e) {
            throw e;
        }
        return singleResult;
    }

    private Institucion buscarInstitucion() {
        Institucion singleResult = null;
        try {
            Query query = getEm().createQuery("SELECT u FROM Institucion u WHERE" + ""
                    + " (u.codigo='MEGAGITEL')");
            singleResult = (Institucion) query.getSingleResult();
        } catch (NoResultException e) {
            throw e;
        }
        return singleResult;
    }

    public EntityManager getEm() {
        return em;
    }

    public void setEm(EntityManager em) {
        this.em = em;
    }

}
