package com.mycompany.controller;

import com.mycompany.model.Promocion;
import com.mycompany.model.Producto;
import com.mycompany.service.PromocionService;
import com.mycompany.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/promociones")
public class PromocionController {
    @Autowired
    private PromocionService promocionService;
    @Autowired
    private ProductoService productoService;

    @GetMapping("/listar")
    public String listar(Model model) {
        List<Promocion> promociones = promocionService.listarTodas();
        model.addAttribute("promociones", promociones);
        return "promociones/listarPromociones";
    }

    @GetMapping("/nueva")
    public String nueva(Model model) {
        List<Producto> productos = productoService.listarTodos();
        model.addAttribute("productos", productos);
        return "promociones/nuevaPromocion";
    }

    @PostMapping("/guardar")
    public String guardar(@RequestParam String nombre,
                         @RequestParam String descripcion,
                         @RequestParam double descuento,
                         @RequestParam Long productoId,
                         @RequestParam String fechaInicio,
                         @RequestParam String fechaFin) {
        Producto producto = productoService.buscarPorId(productoId);
        if (producto == null) return "redirect:/promociones/nueva";
        try {
            Date inicio = java.sql.Date.valueOf(fechaInicio);
            Date fin = java.sql.Date.valueOf(fechaFin);
            Promocion promocion = new Promocion(nombre, descripcion, descuento, producto, inicio, fin);
            promocionService.guardar(promocion);
        } catch (Exception e) {
            return "redirect:/promociones/nueva";
        }
        return "redirect:/promociones/listar";
    }
}
