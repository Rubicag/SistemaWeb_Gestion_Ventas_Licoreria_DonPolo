/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.service;
import com.mycompany.dao.VentaDAO;
import com.mycompany.model.Venta;
import org.springframework.stereotype.Service;
import java.util.List;
/**
 *
 * @author LUIGGI
 */
@Service
public class VentaService {

    private final VentaDAO ventaDAO;

    public VentaService(VentaDAO ventaDAO) {
        this.ventaDAO = ventaDAO;
    }

    // Registrar una venta
    public void registrarVenta(Venta venta) {
        ventaDAO.guardarVenta(venta);
    }

    // Obtener todas las ventas
    public List<Venta> obtenerVentas() {
        return ventaDAO.obtenerVentas();
    }

    // Buscar venta por ID (opcional, útil si quieres detalles individuales)
    public Venta buscarVentaPorId(int id) {
        return ventaDAO.buscarPorId(id);
    }
}