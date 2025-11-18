package com.mycompany.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entidad Promoción - Descuentos y ofertas especiales para productos
 */
@Entity
@Table(name = "promociones", indexes = {
    @Index(name = "idx_promocion_producto", columnList = "id_producto"),
    @Index(name = "idx_promocion_fechas", columnList = "fecha_inicio,fecha_fin")
})
public class Promocion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_promocion")
    private Integer idPromocion;

    @NotBlank(message = "El nombre de la promoción es obligatorio")
    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Size(max = 255, message = "La descripción no puede exceder 255 caracteres")
    @Column(name = "descripcion", length = 255)
    private String descripcion;

    @NotNull(message = "El porcentaje de descuento es obligatorio")
    @DecimalMin(value = "0.01", message = "El descuento debe ser mayor a 0")
    @DecimalMax(value = "100.00", message = "El descuento no puede exceder 100%")
    @Column(name = "descuento", nullable = false, precision = 5, scale = 2)
    private BigDecimal descuento; // Porcentaje de descuento

    @NotNull(message = "La fecha de inicio es obligatoria")
    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @NotNull(message = "La fecha de fin es obligatoria")
    @Column(name = "fecha_fin", nullable = false)
    private LocalDate fechaFin;

    @Column(name = "activo", nullable = false)
    private boolean activo = true;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @NotNull(message = "El producto es obligatorio")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_producto", nullable = false)
    private Producto producto;

    // Constructores
    public Promocion() {
        this.fechaCreacion = LocalDateTime.now();
    }

    public Promocion(String nombre, String descripcion, BigDecimal descuento, 
                    LocalDate fechaInicio, LocalDate fechaFin, Producto producto) {
        this();
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.descuento = descuento;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.producto = producto;
    }

    // Métodos de negocio
    public boolean isVigente() {
        LocalDate hoy = LocalDate.now();
        return activo && 
               !hoy.isBefore(fechaInicio) && 
               !hoy.isAfter(fechaFin);
    }

    public boolean isVigente(LocalDate fecha) {
        return activo && 
               !fecha.isBefore(fechaInicio) && 
               !fecha.isAfter(fechaFin);
    }

    public long getDiasRestantes() {
        LocalDate hoy = LocalDate.now();
        if (hoy.isAfter(fechaFin)) {
            return 0;
        }
        return java.time.temporal.ChronoUnit.DAYS.between(hoy, fechaFin);
    }

    public BigDecimal calcularPrecioConDescuento(BigDecimal precioOriginal) {
        if (!isVigente() || precioOriginal == null) {
            return precioOriginal;
        }
        BigDecimal porcentaje = descuento.divide(new BigDecimal("100"));
        BigDecimal montoDescuento = precioOriginal.multiply(porcentaje);
        return precioOriginal.subtract(montoDescuento);
    }

    @PrePersist
    @PreUpdate
    private void validarFechas() {
        if (fechaFin != null && fechaInicio != null && fechaFin.isBefore(fechaInicio)) {
            throw new IllegalStateException("La fecha de fin no puede ser anterior a la fecha de inicio");
        }
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
    
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
    
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    
    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) { this.producto = producto; }

    @Override
    public String toString() {
        return "Promocion{" +
                "idPromocion=" + idPromocion +
                ", nombre='" + nombre + '\'' +
                ", descuento=" + descuento +
                ", vigente=" + isVigente() +
                '}';
    }
}
