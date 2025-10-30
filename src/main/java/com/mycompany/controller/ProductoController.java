/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.controller;
import com.mycompany.model.Producto;
import com.mycompany.model.Categoria;
import com.mycompany.model.Proveedor;
import com.mycompany.service.ProductoService;
import com.mycompany.service.CategoriaService;
import com.mycompany.service.ProveedorService;
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
    private final CategoriaService categoriaService;
    private final ProveedorService proveedorService;
    // Constructor para inyección de dependencias
    public ProductoController(ProductoService productoService, CategoriaService categoriaService, ProveedorService proveedorService) {
        this.productoService = productoService;
        this.categoriaService = categoriaService;
        this.proveedorService = proveedorService;
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
        model.addAttribute("categorias", categoriaService.listarCategorias());
        model.addAttribute("proveedores", proveedorService.listarProveedores());
        return "productos/agregar";
    }

    // Procesar formulario de agregar producto
    @PostMapping("/agregar")
    public String agregarProducto(@ModelAttribute Producto producto, @RequestParam Integer categoriaId, @RequestParam Integer proveedorId) {
        Categoria categoria = categoriaService.obtenerCategoriaPorId(categoriaId);
        Proveedor proveedor = proveedorService.obtenerProveedorPorId(proveedorId);
        producto.setCategoria(categoria);
        producto.setProveedor(proveedor);
        productoService.agregarProducto(producto);
        return "redirect:/productos/listar";
    }

    // Eliminar producto por ID
    @GetMapping("/eliminar/{id}")
    public String eliminarProducto(@PathVariable("id") int id) {
        productoService.eliminarProducto(id);
        return "redirect:/productos/listar";
    }

    // Mostrar formulario para editar producto
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable("id") int id, Model model) {
        Producto producto = productoService.buscarProductoPorId(id);
        model.addAttribute("producto", producto);
        model.addAttribute("categorias", categoriaService.listarCategorias());
        model.addAttribute("proveedores", proveedorService.listarProveedores());
        return "productos/editar";
    }

    // Procesar formulario de edición de producto
    @PostMapping("/actualizar")
    public String actualizarProducto(@ModelAttribute Producto producto, @RequestParam Integer categoriaId, @RequestParam Integer proveedorId) {
        Categoria categoria = categoriaService.obtenerCategoriaPorId(categoriaId);
        Proveedor proveedor = proveedorService.obtenerProveedorPorId(proveedorId);
        producto.setCategoria(categoria);
        producto.setProveedor(proveedor);
        productoService.agregarProducto(producto); // save() sirve para update también
        return "redirect:/productos/listar";
    }

    // Redirigir /productos/nuevo a /productos/agregar para compatibilidad
    @GetMapping("/nuevo")
    public String redirigirNuevo() {
        return "redirect:/productos/agregar";
    }
}