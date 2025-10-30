package com.mycompany.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import com.mycompany.service.ReporteService;
import com.mycompany.model.Reporte;
import java.util.Optional;

@Controller
@RequestMapping("/reportes")
public class ReporteController {

    private final ReporteService reporteService;

    public ReporteController(ReporteService reporteService) {
        this.reporteService = reporteService;
    }

    @GetMapping("/listar")
    public String listarReportes(Model model) {
        model.addAttribute("reportes", reporteService.obtenerReportes());
        return "reportes/listar";
    }

    @GetMapping("/nuevo")
    public String nuevoReporte(Model model) {
        model.addAttribute("reporte", new Reporte());
        return "reportes/nuevo";
    }

    @PostMapping("/guardar")
    public String guardarReporte(@ModelAttribute Reporte reporte) {
        reporteService.guardarReporte(reporte);
        return "redirect:/reportes/listar";
    }

    @GetMapping("/ver/{id}")
    public String verReporte(@PathVariable Integer id, Model model) {
        Optional<Reporte> reporte = reporteService.obtenerPorId(id);
        if (reporte.isPresent()) {
            model.addAttribute("reporte", reporte.get());
            return "reportes/ver";
        } else {
            return "redirect:/reportes/listar";
        }
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarReporte(@PathVariable Integer id) {
        reporteService.eliminarReporte(id);
        return "redirect:/reportes/listar";
    }

    @GetMapping("")
    public String indexReporte(Model model) {
        return "reportes/index";
    }
}
