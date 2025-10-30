package com.mycompany.controller;

import com.mycompany.model.CarritoDetalle;
import com.mycompany.service.CarritoDetalleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@Controller
@RequestMapping("/carritos/detalle")
public class CarritoDetalleController {
    private final CarritoDetalleService carritoDetalleService;

    public CarritoDetalleController(CarritoDetalleService carritoDetalleService) {
        this.carritoDetalleService = carritoDetalleService;
    }

    @GetMapping("/listar/{idCarrito}")
    public String listarDetalles(@PathVariable Integer idCarrito, Model model) {
        model.addAttribute("detalles", carritoDetalleService.obtenerDetallesPorCarrito(idCarrito));
        model.addAttribute("idCarrito", idCarrito);
        return "carritos/detalle";
    }

    @GetMapping("/nuevo/{idCarrito}")
    public String nuevoDetalle(@PathVariable Integer idCarrito, Model model) {
        CarritoDetalle detalle = new CarritoDetalle();
        detalle.setIdCarrito(idCarrito);
        model.addAttribute("detalle", detalle);
        return "carritos/detalle_nuevo";
    }

    @PostMapping("/guardar")
    public String guardarDetalle(@ModelAttribute CarritoDetalle detalle) {
        carritoDetalleService.guardarDetalle(detalle);
        return "redirect:/carritos/detalle/listar/" + detalle.getIdCarrito();
    }

    @GetMapping("/editar/{id}")
    public String editarDetalle(@PathVariable Integer id, Model model) {
        Optional<CarritoDetalle> detalle = carritoDetalleService.obtenerPorId(id);
        if (detalle.isPresent()) {
            model.addAttribute("detalle", detalle.get());
            return "carritos/detalle_editar";
        } else {
            return "redirect:/carritos/listar";
        }
    }

    @PostMapping("/actualizar")
    public String actualizarDetalle(@ModelAttribute CarritoDetalle detalle) {
        carritoDetalleService.guardarDetalle(detalle);
        return "redirect:/carritos/detalle/listar/" + detalle.getIdCarrito();
    }

    @GetMapping("/eliminar/{id}/{idCarrito}")
    public String eliminarDetalle(@PathVariable Integer id, @PathVariable Integer idCarrito) {
        carritoDetalleService.eliminarDetalle(id);
        return "redirect:/carritos/detalle/listar/" + idCarrito;
    }
}
