package com.mycompany.model;

import jakarta.persistence.*;

@Entity
@Table(name = "carrito_detalle")
public class CarritoDetalle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_carrito_detalle")
    private Integer idCarritoDetalle;

    @Column(name = "id_carrito")
    private Integer idCarrito;

    @Column(name = "id_producto")
    private Integer idProducto;

    @Column(name = "cantidad")
    private Integer cantidad;

    public CarritoDetalle() {}

    public Integer getIdCarritoDetalle() {
        return idCarritoDetalle;
    }
    public void setIdCarritoDetalle(Integer idCarritoDetalle) {
        this.idCarritoDetalle = idCarritoDetalle;
    }
    public Integer getIdCarrito() {
        return idCarrito;
    }
    public void setIdCarrito(Integer idCarrito) {
        this.idCarrito = idCarrito;
    }
    public Integer getIdProducto() {
        return idProducto;
    }
    public void setIdProducto(Integer idProducto) {
        this.idProducto = idProducto;
    }
    public Integer getCantidad() {
        return cantidad;
    }
    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }
}
