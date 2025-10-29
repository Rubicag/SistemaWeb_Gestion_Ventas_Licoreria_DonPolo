package com.mycompany.controller;

import com.mycompany.service.PromocionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/promociones")
public class PromocionController {
    private final PromocionService promocionService;

    public PromocionController(PromocionService promocionService) {
        this.promocionService = promocionService;
    }

    @GetMapping("/listar")
    public String listarPromociones(Model model) {
        model.addAttribute("promociones", promocionService.obtenerPromociones());
        return "promociones/listar";
    }
}
