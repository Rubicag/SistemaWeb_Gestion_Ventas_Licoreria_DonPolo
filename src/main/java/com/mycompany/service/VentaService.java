/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.mycompany.repository.VentaRepository;
import com.mycompany.model.Venta;
import com.mycompany.model.DetalleVenta;
import com.mycompany.model.Producto;
import com.mycompany.repository.DetalleVentaRepository;
import com.mycompany.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import java.util.List;
/**
 *
 * @author LUIGGI
 */
@Service

public class VentaService {
    private static final Logger logger = LoggerFactory.getLogger(VentaService.class);

    // Ejemplo de logging seguro
    public void logOperacionVenta(String usuario) {
        logger.info("Operación de venta realizada por usuario: {}", usuario);
    }
    private final VentaRepository ventaRepository;
    private final DetalleVentaRepository detalleVentaRepository;
    private final ProductoRepository productoRepository;

    public VentaService(VentaRepository ventaRepository, DetalleVentaRepository detalleVentaRepository, ProductoRepository productoRepository) {
        this.ventaRepository = ventaRepository;
        this.detalleVentaRepository = detalleVentaRepository;
        this.productoRepository = productoRepository;
    }

    // Registrar una venta con detalles y actualizar stock
    public void registrarVenta(Venta venta) {
        if (venta.getMetodoPago() == null || venta.getMetodoPago().isEmpty()) {
            throw new IllegalArgumentException("El método de pago es obligatorio");
        }
        // Guardar la venta primero para obtener el ID
        Venta ventaGuardada = ventaRepository.save(venta);
        if (venta.getDetalles() != null) {
            for (DetalleVenta detalle : venta.getDetalles()) {
                // Actualizar stock del producto
                Producto producto = detalle.getProducto();
                if (producto != null) {
                    Producto prodDB = productoRepository.findById(producto.getIdProducto()).orElseThrow();
                    int nuevoStock = prodDB.getCantidad() - detalle.getCantidad();
                    if (nuevoStock < 0) {
                        throw new IllegalArgumentException("Stock insuficiente para el producto: " + prodDB.getNombre());
                    }
                    prodDB.setCantidad(nuevoStock);
                    productoRepository.save(prodDB);
                }
                detalle.setVenta(ventaGuardada);
                detalleVentaRepository.save(detalle);
            }
        }
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