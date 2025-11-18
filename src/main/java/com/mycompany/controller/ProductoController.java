package com.mycompany.controller;

import com.mycompany.dto.ProductoDTO;
import com.mycompany.model.Producto;
import com.mycompany.service.ProductoService;
import com.mycompany.service.CategoriaService;
import com.mycompany.service.ProveedorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * Controlador para gestión de Productos
 * @author LUIGGI
 */
@Controller
@RequestMapping("/productos")
public class ProductoController {
    
    @Autowired
    private ProductoService productoService;
    
    @Autowired
    private CategoriaService categoriaService;
    
    @Autowired
    private ProveedorService proveedorService;

    @GetMapping("")
    public String redirectToListar() {
        return "redirect:/productos/listar";
    }

    @GetMapping("/listar")
    public String listarProductos(Model model) {
        List<ProductoDTO> productos = productoService.convertirListaADTO(productoService.listarActivos());
        model.addAttribute("productos", productos);
        model.addAttribute("categorias", categoriaService.listarActivas());
        model.addAttribute("proveedores", proveedorService.listarActivos());
        return "productos";
    }

    @GetMapping("/detalle/{id}")
    public String detalleProducto(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Producto producto = productoService.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));
            model.addAttribute("producto", productoService.convertirADTO(producto));
            return "productos/detalleProducto";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/productos";
        }
    }

    @GetMapping("/agregar")
    public String agregar(Model model) {
        model.addAttribute("producto", new ProductoDTO());
        model.addAttribute("productos", productoService.listarTodos());
        model.addAttribute("categorias", categoriaService.listarActivas());
        model.addAttribute("proveedores", proveedorService.listarActivos());
        return "productos";
    }

    @PostMapping("/agregar")
    public String agregarProducto(@Valid @ModelAttribute("producto") ProductoDTO productoDTO,
                                  BindingResult result,
                                  Model model,
                                  RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("productos", productoService.listarTodos());
            model.addAttribute("categorias", categoriaService.listarActivas());
            model.addAttribute("proveedores", proveedorService.listarActivos());
            return "productos";
        }

        try {
            Producto producto = productoService.convertirAEntidad(productoDTO);
            productoService.guardar(producto);
            redirectAttributes.addFlashAttribute("mensaje", "Producto guardado exitosamente");
            return "redirect:/productos";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/productos";
        }
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Producto producto = productoService.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));
            model.addAttribute("producto", productoService.convertirADTO(producto));
            model.addAttribute("productos", productoService.listarTodos());
            model.addAttribute("categorias", categoriaService.listarActivas());
            model.addAttribute("proveedores", proveedorService.listarActivos());
            return "productos";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/productos";
        }
    }

    @PostMapping("/actualizar")
    public String actualizarProducto(@Valid @ModelAttribute("producto") ProductoDTO productoDTO,
                                     BindingResult result,
                                     Model model,
                                     RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("productos", productoService.listarTodos());
            model.addAttribute("categorias", categoriaService.listarActivas());
            model.addAttribute("proveedores", proveedorService.listarActivos());
            return "productos";
        }

        try {
            Producto producto = productoService.convertirAEntidad(productoDTO);
            productoService.actualizar(producto);
            redirectAttributes.addFlashAttribute("mensaje", "Producto actualizado exitosamente");
            return "redirect:/productos";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/productos";
        }
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarProducto(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            productoService.eliminar(id);
            redirectAttributes.addFlashAttribute("mensaje", "Producto desactivado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar producto: " + e.getMessage());
        }
        return "redirect:/productos";
    }

    @GetMapping("/bajo-stock")
    public String listarBajoStock(Model model) {
        List<ProductoDTO> productos = productoService.convertirListaADTO(productoService.obtenerProductosConBajoStock());
        model.addAttribute("productos", productos);
        model.addAttribute("categorias", categoriaService.listarActivas());
        model.addAttribute("proveedores", proveedorService.listarActivos());
        model.addAttribute("alerta", "Productos con stock bajo");
        return "productos";
    }

    @GetMapping("/buscar")
    public String buscarProductos(@RequestParam("termino") String termino, Model model) {
        List<Producto> productos = productoService.buscar(termino);
        model.addAttribute("productos", productoService.convertirListaADTO(productos));
        model.addAttribute("categorias", categoriaService.listarActivas());
        model.addAttribute("proveedores", proveedorService.listarActivos());
        model.addAttribute("termino", termino);
        return "productos";
    }

    @GetMapping("/nuevo")
    public String redirigirNuevo() {
        return "redirect:/productos/agregar";
    }
}
