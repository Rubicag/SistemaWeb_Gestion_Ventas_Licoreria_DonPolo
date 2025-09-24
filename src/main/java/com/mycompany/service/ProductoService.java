/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.service;
import org.apache.log4j.Logger;
import com.mycompany.dao.ProductoDAO;
import com.mycompany.model.Producto;
import java.util.List;

/**
 *
 * @author LUIGGI
 */
public class ProductoService {
 private static final Logger logger = Logger.getLogger(ProductoService.class);
    private final ProductoDAO productoDAO;

    // Constructor que recibe el DAO configurado
    public ProductoService(ProductoDAO productoDAO) {
        this.productoDAO = productoDAO;
    }

    // Obtener todos los productos
    public List<Producto> obtenerProductos() {
        logger.info("Obteniendo productos...");
        List<Producto> productos = productoDAO.obtenerTodosLosProductos();
        logger.info("Productos obtenidos: " + productos.size());
        return productos;
    }

    // Agregar un nuevo producto
    public void agregarProducto(Producto producto) {
        logger.debug("Agregando producto: " + producto.getNombre());
        productoDAO.agregarProducto(producto);
        logger.info("Producto agregado: " + producto.getNombre());
    }

    // Eliminar un producto
    public void eliminarProducto(int productoId) {
        logger.debug("Eliminando producto con ID: " + productoId);
        productoDAO.eliminarProducto(productoId);
        logger.info("Producto con ID " + productoId + " eliminado.");
    }
   public Producto buscarProductoPorId(int id) {
    return productoDAO.buscarPorId(id);

}
    }