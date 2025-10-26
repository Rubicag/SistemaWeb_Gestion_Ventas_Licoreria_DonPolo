package com.mycompany.service;

import com.mycompany.model.Proveedor;
import com.mycompany.repository.ProveedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProveedorService {
    @Autowired
    private ProveedorRepository proveedorRepository;

    public List<Proveedor> listarProveedores() {
        return proveedorRepository.findAll();
    }

    public Proveedor guardarProveedor(Proveedor proveedor) {
        return proveedorRepository.save(proveedor);
    }

    public Proveedor obtenerProveedorPorId(Integer id) {
        return proveedorRepository.findById(id).orElse(null);
    }

    public void eliminarProveedor(Integer id) {
        proveedorRepository.deleteById(id);
    }
}
