package com.mycompany.model;

import jakarta.persistence.*;

@Entity
@Table(name = "carrito_detalle")
public class CarritoDetalle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_carrito_detalle")
    private Integer idCarritoDetalle;

    @Column(name = "id_carrito", nullable = false)
    private Integer idCarrito;

    @Column(name = "id_producto", nullable = false)
    private Integer idProducto;

    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    // Getters y setters
    public Integer getIdCarritoDetalle() { return idCarritoDetalle; }
    public void setIdCarritoDetalle(Integer idCarritoDetalle) { this.idCarritoDetalle = idCarritoDetalle; }
    public Integer getIdCarrito() { return idCarrito; }
    public void setIdCarrito(Integer idCarrito) { this.idCarrito = idCarrito; }
    public Integer getIdProducto() { return idProducto; }
    public void setIdProducto(Integer idProducto) { this.idProducto = idProducto; }
    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
}
