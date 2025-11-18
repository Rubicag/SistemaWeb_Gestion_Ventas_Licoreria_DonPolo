package com.mycompany.model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "reportes")
public class Reporte {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reporte")
    private Integer idReporte;

    @Column(name = "nombre", length = 150)
    private String nombre;

    @Lob
    @Column(name = "plantilla")
    private String plantilla;

    @Column(name = "descripcion", length = 500)
    private String descripcion;

    @Temporal(TemporalType.TIMESTAMP)
    private Date creadoAt;

    public Integer getIdReporte() { return idReporte; }
    public void setIdReporte(Integer idReporte) { this.idReporte = idReporte; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getPlantilla() { return plantilla; }
    public void setPlantilla(String plantilla) { this.plantilla = plantilla; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public Date getCreadoAt() { return creadoAt; }
    public void setCreadoAt(Date creadoAt) { this.creadoAt = creadoAt; }
}
