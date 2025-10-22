
package com.mycompany.model;

import jakarta.persistence.*;

@Entity
@Table(name = "productos")
public class Producto {
    // Métodos para compatibilidad con el sistema (setters)
    // Métodos para compatibilidad con el sistema (setters)
    public void setId(Integer idProducto) {
        this.idProducto = idProducto;
    }

    public void setCategoria(Integer idCategoria) {
        this.idCategoria = idCategoria;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto")
    private Integer idProducto;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "precio", nullable = false)
    private double precio;

    @Column(name = "cantidad", nullable = false)
    private int cantidad;

    @Column(name = "id_categoria")
    private Integer idCategoria;

    @Column(name = "id_proveedor")
    private Integer idProveedor;


    // Métodos setters y getters para compatibilidad con el servicio y controlador
    public Integer getIdProducto() {
        return idProducto;
    }
    public void setIdProducto(Integer idProducto) {
        this.idProducto = idProducto;
    }
    public Integer getIdCategoria() {
        return idCategoria;
    }
    public void setIdCategoria(Integer idCategoria) {
        this.idCategoria = idCategoria;
    }
    public Integer getIdProveedor() {
        return idProveedor;
    }
    public void setIdProveedor(Integer idProveedor) {
        this.idProveedor = idProveedor;
    }

    // Métodos para compatibilidad con el sistema
    public Integer getId() {
        return idProducto;
    }

    public Integer getCategoria() {
        return idCategoria;
    }



    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }





    // Métodos getters (si es necesario)



    public String getNombre() {
        return nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public int getCantidad() {
        return cantidad;
    }




    // Método toString opcional para depuración
    @Override
    public String toString() {
     return "Producto{" +
         "idProducto=" + idProducto +
         ", nombre='" + nombre + '\'' +
         ", precio=" + precio +
         ", idCategoria=" + idCategoria +
         ", idProveedor=" + idProveedor +
         ", cantidad=" + cantidad +
         '}';
    }
}
