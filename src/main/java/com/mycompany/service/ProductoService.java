
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.service;

import com.google.common.collect.ImmutableList;

import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.mycompany.repository.ProductoRepository;
import com.mycompany.model.Producto;
import java.util.List;

/**
 *
 * @author LUIGGI
 */
@Service
public class ProductoService {
    private static final Logger logger = LoggerFactory.getLogger(ProductoService.class);
    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    // Ejemplo de uso seguro de Guava: lista inmutable para productos destacados
    public java.util.List<String> obtenerProductosDestacados() {
        return ImmutableList.of("Ron", "Whisky", "Vodka");
    }

    // Obtener todos los productos
    public List<Producto> obtenerProductos() {
        logger.info("Obteniendo productos...");
        List<Producto> productos = productoRepository.findAll();
        logger.info("Productos obtenidos: " + productos.size());
        return productos;
    }

    // Agregar un nuevo producto
    public void agregarProducto(Producto producto) {
        logger.debug("Agregando producto: " + producto.getNombre());
        productoRepository.save(producto);
        logger.info("Producto agregado: " + producto.getNombre());
    }

    // Eliminar un producto
    public void eliminarProducto(int productoId) {
        logger.debug("Eliminando producto con ID: " + productoId);
        productoRepository.deleteById(productoId);
        logger.info("Producto con ID " + productoId + " eliminado.");
    }
    public Producto buscarProductoPorId(int id) {
        return productoRepository.findById(id).orElse(null);
    }
}