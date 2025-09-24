/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.model;
import java.util.Date;
/**
 *
 * @author LUIGGI
 */
public class Venta {

    private int id;
    private int idCliente;
    private int idProducto;
    private int cantidad;
    private double total;
    private Date fecha;

    // Constructor vacío
    public Venta() {
    }

    // Constructor con parámetros
    public Venta(int id, int idCliente, int idProducto, int cantidad, double total, Date fecha) {
        this.id = id;
        this.idCliente = idCliente;
        this.idProducto = idProducto;
        this.cantidad = cantidad;
        this.total = total;
        this.fecha = fecha;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    // Método toString opcional
    @Override
    public String toString() {
        return "Venta{" +
                "id=" + id +
                ", idCliente=" + idCliente +
                ", idProducto=" + idProducto +
                ", cantidad=" + cantidad +
                ", total=" + total +
                ", fecha=" + fecha +
                '}';
    }
}