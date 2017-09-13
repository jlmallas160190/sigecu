/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.megagitel.sigecu.academico.modelo;

import com.megagitel.sigecu.core.modelo.Persona;
import com.megagitel.sigecu.seguridad.modelo.Usuario;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;

/**
 *
 * @author jorgemalla
 */
@Entity
@Table(name = "academico_estudiante")
@Audited
@AuditTable(value = "academico_estudiante_aud", schema = "audit")
public class Estudiante extends Persona implements Serializable{

    @OneToMany(mappedBy = "estudiante", fetch = FetchType.LAZY)
    private List<Matricula> matriculas;
    @OneToMany(mappedBy = "estudiante", fetch = FetchType.LAZY)
    private List<InteresComponenteEducativo> interesComponenteEducativos;

    public Estudiante() {
    }

    public Estudiante(List<Matricula> matriculas) {
        this.matriculas = matriculas;
    }

    public Estudiante(List<Matricula> matriculas, Long id, String numeroIdentificacion,
            String primerNombre, String segundoNombre, String primerApellido,
            String segundoApellido, String email, Date fechaNacimiento, Integer tipoIdentificacion,
            Integer estadoCivil, Integer nivelInstuccion, Integer genero, String celular,
            Usuario usuario) {
        super(id, numeroIdentificacion, primerNombre, segundoNombre, primerApellido,
                segundoApellido, email, fechaNacimiento, tipoIdentificacion,
                estadoCivil, nivelInstuccion, genero, celular, usuario);
        this.matriculas = matriculas;
    }

    public List<Matricula> getMatriculas() {
        return matriculas;
    }

    public void setMatriculas(List<Matricula> matriculas) {
        this.matriculas = matriculas;
    }

    public List<InteresComponenteEducativo> getInteresComponenteEducativos() {
        return interesComponenteEducativos;
    }

    public void setInteresComponenteEducativos(List<InteresComponenteEducativo> interesComponenteEducativos) {
        this.interesComponenteEducativos = interesComponenteEducativos;
    }

}
