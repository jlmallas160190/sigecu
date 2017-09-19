/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.megagitel.sigecu.academico.ejb;

import com.megagitel.sigecu.academico.modelo.Matricula;
import com.megagitel.sigecu.academico.modelo.MatriculaComponenteEducativo;
import com.megagitel.sigecu.dao.AbstractDao;
import java.io.Serializable;
import java.util.List;
import javax.ejb.Stateless;

/**
 *
 * @author jorgemalla
 */
@Stateless
public class MatriculaComponenteEducativoService extends AbstractDao<MatriculaComponenteEducativo> implements Serializable {

    public MatriculaComponenteEducativoService() {
        super(MatriculaComponenteEducativo.class);
    }

    public List<MatriculaComponenteEducativo> getMatriculasMatriculadasByMatricula(Matricula matricula, Integer estado) {
        return this.findByNamedQueryWithLimit("MatriculaComponenteEducativo.findMatriculaEstado", 0, matricula, estado);
    }
}
