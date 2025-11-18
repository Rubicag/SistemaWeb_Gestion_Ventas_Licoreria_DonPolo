package com.mycompany.service;

import com.mycompany.model.Proveedor;
import com.mycompany.repository.ProveedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Servicio para gestión de Proveedores
 */
@Service
@Transactional
public class ProveedorService {
    @Autowired
    private ProveedorRepository proveedorRepository;

    public List<Proveedor> listarTodos() {
        return proveedorRepository.findAll();
    }

    public List<Proveedor> listarActivos() {
        return proveedorRepository.findProveedoresActivos();
    }

    public Optional<Proveedor> buscarPorId(Integer id) {
        return proveedorRepository.findById(id);
    }

    public Optional<Proveedor> buscarPorRuc(String ruc) {
        return proveedorRepository.findByRuc(ruc);
    }

    public List<Proveedor> buscar(String termino) {
        return proveedorRepository.buscarPorNombre(termino);
    }

    public Proveedor guardar(Proveedor proveedor) {
        validarProveedor(proveedor);
        return proveedorRepository.save(proveedor);
    }

    public Proveedor actualizar(Proveedor proveedor) {
        if (proveedor.getIdProveedor() == null) {
            throw new IllegalArgumentException("El ID del proveedor es obligatorio para actualizar");
        }
        if (!proveedorRepository.existsById(proveedor.getIdProveedor())) {
            throw new IllegalArgumentException("Proveedor no encontrado con ID: " + proveedor.getIdProveedor());
        }
        validarProveedor(proveedor);
        return proveedorRepository.save(proveedor);
    }

    public void eliminar(Integer id) {
        Proveedor proveedor = proveedorRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Proveedor no encontrado con ID: " + id));
        proveedor.setEstado("INACTIVO");
        proveedorRepository.save(proveedor);
    }

    public void activar(Integer id) {
        Proveedor proveedor = proveedorRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Proveedor no encontrado con ID: " + id));
        proveedor.setEstado("ACTIVO");
        proveedorRepository.save(proveedor);
    }

    public long contarActivos() {
        return proveedorRepository.contarProveedoresActivos();
    }

    public List<Proveedor> obtenerProveedoresConMasProductos() {
        return proveedorRepository.findProveedoresConMasProductos();
    }

    private void validarProveedor(Proveedor proveedor) {
        if (proveedor.getNombre() == null || proveedor.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del proveedor es obligatorio");
        }

        // Validar RUC único si está presente
        if (proveedor.getRuc() != null && !proveedor.getRuc().trim().isEmpty()) {
            Optional<Proveedor> existente = proveedorRepository.findByRuc(proveedor.getRuc());
            if (existente.isPresent() && !existente.get().getIdProveedor().equals(proveedor.getIdProveedor())) {
                throw new IllegalArgumentException("Ya existe un proveedor con RUC: " + proveedor.getRuc());
            }
        }
    }

    // Métodos legacy para compatibilidad
    @Deprecated
    public List<Proveedor> listarProveedores() {
        return listarTodos();
    }

    @Deprecated
    public Proveedor guardarProveedor(Proveedor proveedor) {
        return guardar(proveedor);
    }

    @Deprecated
    public Proveedor obtenerProveedorPorId(Integer id) {
        return buscarPorId(id).orElse(null);
    }

    @Deprecated
    public void eliminarProveedor(Integer id) {
        eliminar(id);
    }
}
