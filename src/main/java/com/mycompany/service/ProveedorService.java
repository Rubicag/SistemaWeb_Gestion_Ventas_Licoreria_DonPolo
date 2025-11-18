package com.mycompany.service;

import com.mycompany.model.Proveedor;
import com.mycompany.repository.ProveedorRepository;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
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

    // Ejemplo de lectura segura de archivo con Apache Commons IO
    public String leerArchivoProveedor(String ruta) throws IOException {
        return FileUtils.readFileToString(new File(ruta), "UTF-8");
    }
}
