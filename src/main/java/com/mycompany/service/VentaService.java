/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.service;
import com.mycompany.repository.VentaRepository;
import com.mycompany.model.Venta;
import org.springframework.stereotype.Service;
import java.util.List;
/**
 *
 * @author LUIGGI
 */
@Service
public class VentaService {
    // Actualizar una venta
    public void actualizarVenta(Venta venta) {
        ventaRepository.save(venta);
    }

    private final VentaRepository ventaRepository;

    public VentaService(VentaRepository ventaRepository) {
        this.ventaRepository = ventaRepository;
    }

    // Registrar una venta
    public void registrarVenta(Venta venta) {
        ventaRepository.save(venta);
    }

    // Obtener todas las ventas
    public List<Venta> obtenerVentas() {
        return ventaRepository.findAll();
    }

    // Buscar venta por ID (opcional, útil si quieres detalles individuales)
    public Venta buscarVentaPorId(int id) {
        return ventaRepository.findById(id).orElse(null);
    }
}