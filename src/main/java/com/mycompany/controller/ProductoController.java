/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.controller;
import com.mycompany.model.Producto;
import com.mycompany.service.ProductoService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import java.util.List;

/**
 *
 * @author LUIGGI
 */
@Controller
@RequestMapping("/productos")
public class ProductoController {
    private final ProductoService productoService;
    // Constructor para inyección de dependencias
    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    // Redirigir /productos a /productos/listar
    @GetMapping("")
    public String redirectToListar() {
        return "redirect:/productos/listar";
    }

     // Listar todos los productos
    @GetMapping("/listar")
    public String listarProductos(Model model) {
        List<Producto> productos = productoService.obtenerProductos();
        model.addAttribute("productos", productos);
        return "productos/listar";
    }


    // Mostrar detalle de un producto
    @GetMapping("/detalle/{id}")
    public String detalleProducto(@PathVariable("id") int id, Model model) {
        Producto producto = productoService.buscarProductoPorId(id);
        model.addAttribute("producto", producto);
        return "productos/detalleProducto"; // Corrige la ruta a la carpeta correcta
    }

    // Mostrar formulario para agregar producto
    @GetMapping("/agregar")
    public String mostrarFormularioAgregar(Model model) {
        model.addAttribute("producto", new Producto());
        return "productos/agregar"; // Corrige la ruta a la carpeta correcta
    }

    // Procesar formulario de agregar producto
    @PostMapping("/agregar")
    public String agregarProducto(@ModelAttribute Producto producto) {
        productoService.agregarProducto(producto);
        return "redirect:/productos/listar";
    }

    // Eliminar producto por ID
    @GetMapping("/eliminar/{id}")
    public String eliminarProducto(@PathVariable("id") int id) {
        productoService.eliminarProducto(id);
        return "redirect:/productos/listar";
    }
}