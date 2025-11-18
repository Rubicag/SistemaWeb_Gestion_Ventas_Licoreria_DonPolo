package com.mycompany.dto;

import java.io.Serializable;

/**
 * DTO simple de Cliente para evitar referencias circulares en serialización JSON
 * No incluye la lista de ventas para prevenir ciclos Cliente → Venta → Usuario → Venta
 */
public class ClienteSimpleDTO implements Serializable {
    private Integer idCliente;
    private String nombre;
    private String apellido;
    private String dni;
    private String email;
    private String telefono;
    private String direccion;
    private String nombreCompleto;

    // Constructor vacío
    public ClienteSimpleDTO() {
    }

    // Constructor completo
    public ClienteSimpleDTO(Integer idCliente, String nombre, String apellido, 
                           String dni, String email, String telefono, String direccion) {
        this.idCliente = idCliente;
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.email = email;
        this.telefono = telefono;
        this.direccion = direccion;
        this.nombreCompleto = nombre + " " + apellido;
    }

    // Getters y Setters
    public Integer getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
        updateNombreCompleto();
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
        updateNombreCompleto();
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
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

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    private void updateNombreCompleto() {
        if (nombre != null && apellido != null) {
            this.nombreCompleto = nombre + " " + apellido;
        }
    }

    @Override
    public String toString() {
        return "ClienteSimpleDTO{" +
                "idCliente=" + idCliente +
                ", nombreCompleto='" + nombreCompleto + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
