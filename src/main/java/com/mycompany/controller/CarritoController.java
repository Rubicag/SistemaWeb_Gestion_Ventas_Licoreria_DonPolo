package com.mycompany.controller;

import com.mycompany.model.Carrito;
import com.mycompany.service.CarritoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@Controller
@RequestMapping("/carritos")
public class CarritoController {
    private final CarritoService carritoService;

    public CarritoController(CarritoService carritoService) {
        this.carritoService = carritoService;
    }

    @GetMapping("/listar")
    public String listarCarritos(Model model) {
        var carritos = carritoService.obtenerCarritos();
        // Formatear la fecha en el backend
        java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        var carritosFormateados = carritos.stream().map(c -> {
            var map = new java.util.HashMap<String, Object>();
            map.put("idCarrito", c.getIdCarrito());
            map.put("idUsuario", c.getIdUsuario());
            map.put("estado", c.getEstado());
            map.put("fechaCreacion", c.getFechaCreacion() != null ? c.getFechaCreacion().format(formatter) : "");
            return map;
        }).toList();
        model.addAttribute("carritos", carritosFormateados);
        return "carritos/listar";
    }

    @GetMapping("/nuevo")
    public String nuevoCarrito(Model model) {
        model.addAttribute("carrito", new Carrito());
        return "carritos/nuevo";
    }

    @PostMapping("/guardar")
    public String guardarCarrito(@ModelAttribute Carrito carrito) {
        carritoService.guardarCarrito(carrito);
        return "redirect:/carritos/listar";
    }

    @GetMapping("/editar/{id}")
    public String editarCarrito(@PathVariable Integer id, Model model) {
        Optional<Carrito> carrito = carritoService.obtenerPorId(id);
        if (carrito.isPresent()) {
            model.addAttribute("carrito", carrito.get());
            return "carritos/editar";
        } else {
            return "redirect:/carritos/listar";
        }
    }

    @PostMapping("/actualizar")
    public String actualizarCarrito(@ModelAttribute Carrito carrito) {
        carritoService.guardarCarrito(carrito);
        return "redirect:/carritos/listar";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarCarrito(@PathVariable Integer id) {
        carritoService.eliminarCarrito(id);
        return "redirect:/carritos/listar";
    }
}
