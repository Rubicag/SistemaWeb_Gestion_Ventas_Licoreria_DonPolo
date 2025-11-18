package com.mycompany.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad Venta - Representa una transacción de venta en la licorería
 */
@Entity
@Table(name = "ventas", indexes = {
    @Index(name = "idx_venta_fecha", columnList = "fecha"),
    @Index(name = "idx_venta_cliente", columnList = "id_cliente"),
    @Index(name = "idx_venta_usuario", columnList = "id_usuario")
})
public class Venta {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_venta")
    private Integer idVenta;

    @NotNull(message = "El usuario vendedor es obligatorio")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario; // Empleado que realiza la venta

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_cliente")
    private Cliente cliente; // Cliente (puede ser null para venta sin registro)

    @NotNull(message = "La fecha de venta es obligatoria")
    @Column(name = "fecha", nullable = false)
    private LocalDateTime fecha;

    @NotBlank(message = "El método de pago es obligatorio")
    @Pattern(regexp = "EFECTIVO|TARJETA|YAPE|PLIN|TRANSFERENCIA", message = "Método de pago inválido")
    @Column(name = "metodo_pago", nullable = false, length = 50)
    private String metodoPago; // EFECTIVO, TARJETA, YAPE, PLIN, TRANSFERENCIA

    @NotNull(message = "El total es obligatorio")
    @DecimalMin(value = "0.01", message = "El total debe ser mayor a 0")
    @Column(name = "total", nullable = false, precision = 10, scale = 2)
    private BigDecimal total;

    @DecimalMin(value = "0.00", message = "El descuento no puede ser negativo")
    @Column(name = "descuento", precision = 10, scale = 2)
    private BigDecimal descuento = BigDecimal.ZERO;

    @Column(name = "comprobante", length = 50)
    private String comprobante; // Número de boleta/factura

    @Column(name = "estado", nullable = false, length = 20)
    private String estado = "COMPLETADA"; // COMPLETADA, ANULADA, PENDIENTE

    @Column(name = "observaciones", length = 500)
    private String observaciones;

    @Column(name = "id_carrito")
    private Integer idCarrito;

    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<DetalleVenta> detalles = new ArrayList<>();

    // Constructores
    public Venta() {
        this.fecha = LocalDateTime.now();
    }

    public Venta(Usuario usuario, Cliente cliente, String metodoPago) {
        this();
        this.usuario = usuario;
        this.cliente = cliente;
        this.metodoPago = metodoPago;
        this.total = BigDecimal.ZERO;
    }

    // Métodos de negocio
    public void agregarDetalle(DetalleVenta detalle) {
        detalles.add(detalle);
        detalle.setVenta(this);
        recalcularTotal();
    }

    public void eliminarDetalle(DetalleVenta detalle) {
        detalles.remove(detalle);
        detalle.setVenta(null);
        recalcularTotal();
    }

    public void recalcularTotal() {
        this.total = detalles.stream()
            .map(DetalleVenta::getSubtotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        if (descuento != null && descuento.compareTo(BigDecimal.ZERO) > 0) {
            this.total = this.total.subtract(descuento);
        }
    }

    public BigDecimal getSubtotal() {
        return detalles.stream()
            .map(DetalleVenta::getSubtotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public int getCantidadItems() {
        return detalles.size();
    }

    public int getCantidadTotalProductos() {
        return detalles.stream()
            .mapToInt(DetalleVenta::getCantidad)
            .sum();
    }

    public boolean isCompletada() {
        return "COMPLETADA".equals(estado);
    }

    public boolean isAnulada() {
        return "ANULADA".equals(estado);
    }

    public void anular() {
        if (isAnulada()) {
            throw new IllegalStateException("La venta ya está anulada");
        }
        this.estado = "ANULADA";
    }

    @PrePersist
    @PreUpdate
    private void validarDatos() {
        if (detalles == null || detalles.isEmpty()) {
            throw new IllegalStateException("La venta debe tener al menos un detalle");
        }
        recalcularTotal();
    }

    // Getters y Setters
    public Integer getIdVenta() { return idVenta; }
    public void setIdVenta(Integer idVenta) { this.idVenta = idVenta; }

    // Método de compatibilidad para controladores
    public Integer getId() { return idVenta; }
    public void setId(Integer id) { this.idVenta = id; }
    
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    
    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }
    
    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }
    
    public String getMetodoPago() { return metodoPago; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }
    
    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }
    
    public BigDecimal getDescuento() { return descuento; }
    public void setDescuento(BigDecimal descuento) { this.descuento = descuento; }
    
    public String getComprobante() { return comprobante; }
    public void setComprobante(String comprobante) { this.comprobante = comprobante; }
    
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    
    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
    public Integer getIdCarrito() { return idCarrito; }
    public void setIdCarrito(Integer idCarrito) { this.idCarrito = idCarrito; }
    
    public List<DetalleVenta> getDetalles() { return detalles; }
    public void setDetalles(List<DetalleVenta> detalles) { 
        this.detalles = detalles;
        recalcularTotal();
    }

    @Override
    public String toString() {
        return "Venta{" +
                "idVenta=" + idVenta +
                ", usuario=" + (usuario != null ? usuario.getNombre() : null) +
                ", cliente=" + (cliente != null ? cliente.getNombreCompleto() : "Anónimo") +
                ", total=" + total +
                ", fecha=" + fecha +
                ", estado='" + estado + '\'' +
                '}';
    }
}