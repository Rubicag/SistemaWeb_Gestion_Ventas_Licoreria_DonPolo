package com.mycompany.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad Proveedor - Distribuidores y proveedores de licores
 */
@Entity
@Table(name = "proveedores", indexes = {
    @Index(name = "idx_proveedor_ruc", columnList = "ruc"),
    @Index(name = "idx_proveedor_nombre", columnList = "nombre")
})
public class Proveedor {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_proveedor")
    private Integer idProveedor;

    @NotBlank(message = "El nombre del proveedor es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Pattern(regexp = "^[0-9]{11}$", message = "El RUC debe tener 11 dígitos")
    @Column(name = "ruc", unique = true, length = 11)
    private String ruc;

    @Email(message = "El email debe ser válido")
    @Column(name = "email", length = 100)
    private String email;

    @Pattern(regexp = "^[0-9]{9}$", message = "El teléfono debe tener 9 dígitos")
    @Column(name = "telefono", length = 20)
    private String telefono;

    @Size(max = 255, message = "La dirección no puede exceder 255 caracteres")
    @Column(name = "direccion", length = 255)
    private String direccion;

    @Column(name = "estado", nullable = false, length = 20)
    private String estado = "ACTIVO"; // ACTIVO, INACTIVO

    @Column(name = "contacto", length = 100)
    private String contacto; // Nombre del contacto principal

    @OneToMany(mappedBy = "proveedor", fetch = FetchType.LAZY)
    private List<Producto> productos = new ArrayList<>();

    // Constructores
    public Proveedor() {}

    public Proveedor(String nombre, String ruc, String email, String telefono, String direccion) {
        this.nombre = nombre;
        this.ruc = ruc;
        this.email = email;
        this.telefono = telefono;
        this.direccion = direccion;
    }

    // Métodos de utilidad
    public boolean isActivo() {
        return "ACTIVO".equals(estado);
    }

    public int getCantidadProductos() {
        return productos != null ? productos.size() : 0;
    }

    // Getters y Setters
    public Integer getIdProveedor() { return idProveedor; }
    public void setIdProveedor(Integer idProveedor) { this.idProveedor = idProveedor; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public String getRuc() { return ruc; }
    public void setRuc(String ruc) { this.ruc = ruc; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    
    public String getContacto() { return contacto; }
    public void setContacto(String contacto) { this.contacto = contacto; }
    
    public List<Producto> getProductos() { return productos; }
    public void setProductos(List<Producto> productos) { this.productos = productos; }

    @Override
    public String toString() {
        return "Proveedor{" +
                "idProveedor=" + idProveedor +
                ", nombre='" + nombre + '\'' +
                ", ruc='" + ruc + '\'' +
                ", estado='" + estado + '\'' +
                '}';
    }
}
