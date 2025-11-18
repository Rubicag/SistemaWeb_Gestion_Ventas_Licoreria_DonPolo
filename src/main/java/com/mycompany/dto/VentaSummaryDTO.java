package com.mycompany.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class VentaSummaryDTO {
    private Integer idVenta;
    private LocalDateTime fecha;
    private BigDecimal total;
    private String nombreCliente;
    private String estado;
    private String metodoPago;
    private Integer idCarrito;
    private String comprobante;

    public VentaSummaryDTO(Integer idVenta, LocalDateTime fecha, BigDecimal total, String nombreCliente, String estado, String metodoPago, Integer idCarrito, String comprobante) {
        this.idVenta = idVenta;
        this.fecha = fecha;
        this.total = total;
        this.nombreCliente = nombreCliente;
        this.estado = estado;
        this.metodoPago = metodoPago;
        this.idCarrito = idCarrito;
        this.comprobante = comprobante;
    }

    public Integer getIdVenta() { return idVenta; }
    public LocalDateTime getFecha() { return fecha; }
    public BigDecimal getTotal() { return total; }
    public String getNombreCliente() { return nombreCliente; }
    public String getEstado() { return estado; }
    public String getMetodoPago() { return metodoPago; }
    public Integer getIdCarrito() { return idCarrito; }
    public String getComprobante() { return comprobante; }
}
