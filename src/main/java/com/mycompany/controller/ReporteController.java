package com.mycompany.controller;

import com.mycompany.service.ReporteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/reportes")
public class ReporteController {
    @Autowired
    private ReporteService reporteService;

    @GetMapping("/listar")
    public String listarReportes(Model model) {
        model.addAttribute("reportes", reporteService.listarReportes());
        return "reportes/listar";
    }
}
