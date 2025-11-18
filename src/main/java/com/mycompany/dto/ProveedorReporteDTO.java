package com.mycompany.dto;

/**
 * DTO para reporte de proveedores - evita referencias circulares en serialización JSON
 */
public class ProveedorReporteDTO {
    private Integer idProveedor;
    private String nombre;
    private String ruc;
    private String email;
    private String telefono;
    private String contacto;
    private Integer totalProductos;
    private String estado;

    // Constructor vacío
    public ProveedorReporteDTO() {
    }

    // Constructor completo
    public ProveedorReporteDTO(Integer idProveedor, String nombre, String ruc, String email, 
                              String telefono, String contacto, Integer totalProductos, String estado) {
        this.idProveedor = idProveedor;
        this.nombre = nombre;
        this.ruc = ruc;
        this.email = email;
        this.telefono = telefono;
        this.contacto = contacto;
        this.totalProductos = totalProductos;
        this.estado = estado;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getContacto() {
        return contacto;
    }

    public void setContacto(String contacto) {
        this.contacto = contacto;
    }

    public Integer getTotalProductos() {
        return totalProductos;
    }

    public void setTotalProductos(Integer totalProductos) {
        this.totalProductos = totalProductos;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
