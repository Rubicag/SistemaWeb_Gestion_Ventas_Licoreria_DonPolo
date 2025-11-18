package com.mycompany.dto;

import java.io.Serializable;

/**
 * DTO simple de Usuario para evitar referencias circulares en serialización JSON
 * No incluye la lista de ventas para prevenir ciclos Usuario → Venta → Usuario
 */
public class UsuarioSimpleDTO implements Serializable {
    private Integer idUsuario;
    private String nombre;
    private String correo;
    private String rol;
    private boolean activo;

    // Constructor vacío
    public UsuarioSimpleDTO() {
    }

    // Constructor completo
    public UsuarioSimpleDTO(Integer idUsuario, String nombre, String correo, 
                           String rol, boolean activo) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.correo = correo;
        this.rol = rol;
        this.activo = activo;
    }

    // Getters y Setters
    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    @Override
    public String toString() {
        return "UsuarioSimpleDTO{" +
                "idUsuario=" + idUsuario +
                ", nombre='" + nombre + '\'' +
                ", correo='" + correo + '\'' +
                ", rol='" + rol + '\'' +
                ", activo=" + activo +
                '}';
    }
}
