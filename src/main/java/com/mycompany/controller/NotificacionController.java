package com.mycompany.controller;

import com.mycompany.model.Notificacion;
import com.mycompany.service.NotificacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/notificaciones")
public class NotificacionController {
    @Autowired
    private NotificacionService notificacionService;

    @GetMapping("/listar")
    public String listar(Model model) {
        List<Notificacion> notificaciones = notificacionService.listarTodas();
        model.addAttribute("notificaciones", notificaciones);
        return "notificaciones/listarNotificaciones";
    }

    @GetMapping("/marcar-leida/{id}")
    public String marcarLeida(@PathVariable Long id) {
        notificacionService.marcarComoLeida(id);
        return "redirect:/notificaciones/listar";
    }

    // Ejemplo: crear notificación manual (puedes eliminarlo en producción)
    @GetMapping("/crear-ejemplo")
    public String crearEjemplo() {
        Notificacion n = new Notificacion("Notificación de ejemplo", new Date(), false);
        notificacionService.guardar(n);
        return "redirect:/notificaciones/listar";
    }
}
