package com.mycompany.dto;

import java.io.Serializable;

/**
 * DTO simple de Proveedor para evitar referencias circulares en serialización JSON
 * No incluye la lista de productos para prevenir ciclos Proveedor → Producto → Categoria
 */
public class ProveedorSimpleDTO implements Serializable {
    private Integer idProveedor;
    private String nombre;
    private String ruc;
    private String contacto;
    private String telefono;
    private String email;
    private String direccion;
    private boolean activo;

    // Constructor vacío
    public ProveedorSimpleDTO() {
    }

    // Constructor completo
    public ProveedorSimpleDTO(Integer idProveedor, String nombre, String ruc, String contacto,
                             String telefono, String email, String direccion, boolean activo) {
        this.idProveedor = idProveedor;
        this.nombre = nombre;
        this.ruc = ruc;
        this.contacto = contacto;
        this.telefono = telefono;
        this.email = email;
        this.direccion = direccion;
        this.activo = activo;
    }

    // Getters y Setters
    public Integer getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(Integer idProveedor) {
        this.idProveedor = idProveedor;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public String getContacto() {
        return contacto;
    }

    public void setContacto(String contacto) {
        this.contacto = contacto;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    @Override
    public String toString() {
        return "ProveedorSimpleDTO{" +
                "idProveedor=" + idProveedor +
                ", nombre='" + nombre + '\'' +
                ", contacto='" + contacto + '\'' +
                ", telefono='" + telefono + '\'' +
                ", activo=" + activo +
                '}';
    }
}
