package com.mycompany.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO para reporte de ventas - evita referencias circulares en serialización JSON
 */
public class VentaReporteDTO {
    private Integer idVenta;
    private LocalDateTime fecha;
    private String nombreCliente;
    private String nombreVendedor;
    private String metodoPago;
    private BigDecimal total;
    private String estado;

    // Constructor vacío
    public VentaReporteDTO() {
    }

    // Constructor completo
    public VentaReporteDTO(Integer idVenta, LocalDateTime fecha, String nombreCliente, 
                          String nombreVendedor, String metodoPago, BigDecimal total, String estado) {
        this.idVenta = idVenta;
        this.fecha = fecha;
        this.nombreCliente = nombreCliente;
        this.nombreVendedor = nombreVendedor;
        this.metodoPago = metodoPago;
        this.total = total;
        this.estado = estado;
    }

    // Getters y Setters
    public Integer getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(Integer idVenta) {
        this.idVenta = idVenta;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getNombreVendedor() {
        return nombreVendedor;
    }

    public void setNombreVendedor(String nombreVendedor) {
        this.nombreVendedor = nombreVendedor;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
