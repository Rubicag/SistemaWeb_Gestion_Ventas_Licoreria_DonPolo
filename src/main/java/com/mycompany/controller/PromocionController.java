package com.mycompany.controller;

import com.mycompany.model.Promocion;
import com.mycompany.service.PromocionService;
import com.mycompany.service.ProductoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/promociones")
public class PromocionController {
    private final PromocionService promocionService;
    
    @Autowired
    private ProductoService productoService;

    public PromocionController(PromocionService promocionService) {
        this.promocionService = promocionService;
    }

    @GetMapping({"", "/", "/listar"})
    public String listarPromociones(Model model) {
        model.addAttribute("promociones", promocionService.obtenerPromociones());
        model.addAttribute("productos", productoService.listarTodos());
        return "promociones";
    }

    @PostMapping("/guardar")
    public String guardarPromocion(@Valid @ModelAttribute("promocion") Promocion promocion,
                                   BindingResult result,
                                   Model model,
                                   RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("promociones", promocionService.obtenerPromociones());
            model.addAttribute("productos", productoService.listarTodos());
            return "promociones";
        }

        try {
            promocionService.guardarPromocion(promocion);
            redirectAttributes.addFlashAttribute("mensaje", "Promoción guardada exitosamente");
            return "redirect:/promociones";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/promociones";
        }
    }

    @PostMapping("/actualizar")
    public String actualizarPromocion(@Valid @ModelAttribute("promocion") Promocion promocion,
                                      BindingResult result,
                                      Model model,
                                      RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("promociones", promocionService.obtenerPromociones());
            model.addAttribute("productos", productoService.listarTodos());
            return "promociones";
        }

        try {
            promocionService.actualizar(promocion);
            redirectAttributes.addFlashAttribute("mensaje", "Promoción actualizada exitosamente");
            return "redirect:/promociones";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/promociones";
        }
    }

    @GetMapping("/finalizar/{id}")
    public String finalizarPromocion(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            promocionService.desactivar(id);
            redirectAttributes.addFlashAttribute("mensaje", "Promoción finalizada exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al finalizar promoción: " + e.getMessage());
        }
        return "redirect:/promociones";
    }
}
