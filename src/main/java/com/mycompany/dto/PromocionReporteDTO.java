package com.mycompany.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO para reportes de promociones
 */
public class PromocionReporteDTO {
    private Integer idPromocion;
    private String nombre;
    private String descripcion;
    private BigDecimal descuento;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String nombreProducto;
    private BigDecimal precioOriginal;
    private BigDecimal precioConDescuento;
    private boolean vigente;
    private Long diasRestantes;
    private String estado;

    public PromocionReporteDTO() {}

    public PromocionReporteDTO(Integer idPromocion, String nombre, String descripcion,
                              BigDecimal descuento, LocalDate fechaInicio, LocalDate fechaFin,
                              String nombreProducto, BigDecimal precioOriginal,
                              BigDecimal precioConDescuento, boolean vigente,
                              Long diasRestantes, String estado) {
        this.idPromocion = idPromocion;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.descuento = descuento;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.nombreProducto = nombreProducto;
        this.precioOriginal = precioOriginal;
        this.precioConDescuento = precioConDescuento;
        this.vigente = vigente;
        this.diasRestantes = diasRestantes;
        this.estado = estado;
    }

    // Getters y Setters
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

    public String getNombreProducto() { return nombreProducto; }
    public void setNombreProducto(String nombreProducto) { this.nombreProducto = nombreProducto; }

    public BigDecimal getPrecioOriginal() { return precioOriginal; }
    public void setPrecioOriginal(BigDecimal precioOriginal) { this.precioOriginal = precioOriginal; }

    public BigDecimal getPrecioConDescuento() { return precioConDescuento; }
    public void setPrecioConDescuento(BigDecimal precioConDescuento) { this.precioConDescuento = precioConDescuento; }

    public boolean isVigente() { return vigente; }
    public void setVigente(boolean vigente) { this.vigente = vigente; }

    public Long getDiasRestantes() { return diasRestantes; }
    public void setDiasRestantes(Long diasRestantes) { this.diasRestantes = diasRestantes; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
