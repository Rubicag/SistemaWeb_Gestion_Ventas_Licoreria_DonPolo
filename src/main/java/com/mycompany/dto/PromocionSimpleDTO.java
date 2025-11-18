package com.mycompany.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO simplificado para vistas de Promociones
 */
public class PromocionSimpleDTO {
    private Integer idPromocion;
    private String nombre;
    private String descripcion;
    private BigDecimal descuento;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String tipoPromocion;
    private String estado;

    public PromocionSimpleDTO() {}

    public PromocionSimpleDTO(Integer idPromocion, String nombre, String descripcion,
                              BigDecimal descuento, LocalDate fechaInicio, LocalDate fechaFin,
                              String tipoPromocion, String estado) {
        this.idPromocion = idPromocion;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.descuento = descuento;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.tipoPromocion = tipoPromocion;
        this.estado = estado;
    }

    public Integer getIdPromocion() { return idPromocion; }
    public void setIdPromocion(Integer idPromocion) { this.idPromocion = idPromocion; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public BigDecimal getDescuento() { return descuento; }
    public void setDescuento(BigDecimal descuento) { this.descuento = descuento; }

    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }

    public LocalDate getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDate fechaFin) { this.fechaFin = fechaFin; }

    public String getTipoPromocion() { return tipoPromocion; }
    public void setTipoPromocion(String tipoPromocion) { this.tipoPromocion = tipoPromocion; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
