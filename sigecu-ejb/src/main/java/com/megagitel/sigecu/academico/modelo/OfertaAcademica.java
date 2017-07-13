/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.megagitel.sigecu.academico.modelo;

import com.megagitel.sigecu.core.modelo.Institucion;
import com.megagitel.sigecu.util.Formateador;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.validation.constraints.NotNull;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;

/**
 *
 * @author jorgemalla
 */
@Entity
@Table(name = "academico_oferta_academica")
@Audited
@AuditTable(value = "academico_oferta_academica_aud", schema = "audit")
@NamedQueries({
    @NamedQuery(name = "OfertaAcademica.findCurrent", query = "select c FROM OfertaAcademica c where ?1 BETWEEN c.fechaInicio and c.fechaFin")
})
public class OfertaAcademica implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Long id;
    @Temporal(javax.persistence.TemporalType.DATE)
    @NotNull
    @Column(name = "fecha_inicio")
    private Date fechaInicio;
    @NotNull
    @Temporal(javax.persistence.TemporalType.DATE)
    @Column(name = "fecha_fin")
    private Date fechaFin;
    @Column(name = "descripcion", columnDefinition = "text")
    private String descripcion;
    @ManyToOne
    private Institucion institucion;
    @OneToMany(mappedBy = "ofertaAcademica", fetch = FetchType.LAZY)
    private List<OfertadorComponenteEducativo> ofertadorComponenteEducativos;
    @OneToMany(mappedBy = "ofertaAcademica", fetch = FetchType.LAZY)
    private List<Matricula> matriculas;

    public OfertaAcademica() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Institucion getInstitucion() {
        return institucion;
    }

    public void setInstitucion(Institucion institucion) {
        this.institucion = institucion;
    }

    public List<OfertadorComponenteEducativo> getOfertadorComponenteEducativos() {
        return ofertadorComponenteEducativos;
    }

    public void setOfertadorComponenteEducativos(List<OfertadorComponenteEducativo> ofertadorComponenteEducativos) {
        this.ofertadorComponenteEducativos = ofertadorComponenteEducativos;
    }

    public List<Matricula> getMatriculas() {
        return matriculas;
    }

    public void setMatriculas(List<Matricula> matriculas) {
        this.matriculas = matriculas;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof OfertaAcademica)) {
            return false;
        }
        OfertaAcademica other = (OfertaAcademica) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return this.id + "";
    }

    public String getDetalle() {
        Formateador formateador = new Formateador();
        String fechaInicioFormateada = formateador.fecha(this.fechaInicio, "MMMM-yyy");
        String fechaFinFormateada = formateador.fecha(this.fechaFin, "MMMM-yyy");
        return fechaInicioFormateada + " - " + fechaFinFormateada;
    }
}
