package com.mycompany.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "reportes")
public class Reporte {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reporte")
    private Integer idReporte;

    @Column(name = "titulo", nullable = false, length = 100)
    private String titulo;

    @Column(name = "descripcion", length = 500)
    private String descripcion;

    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    // Getters y setters
    public Integer getIdReporte() { return idReporte; }
    public void setIdReporte(Integer idReporte) { this.idReporte = idReporte; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
}
