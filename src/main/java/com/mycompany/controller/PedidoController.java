package com.mycompany.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.mycompany.service.PedidoService;
import com.mycompany.service.UsuarioService;

@Controller
@RequestMapping("/pedidos")
public class PedidoController {
    private final PedidoService pedidoService;
    
    @Autowired
    private UsuarioService usuarioService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @GetMapping({"", "/", "/listar"})
    public String listarPedidos(Model model) {
        model.addAttribute("pedidos", pedidoService.obtenerPedidos());
        model.addAttribute("usuarios", usuarioService.obtenerUsuarios());
        return "pedidos";
    }

    // Métodos para guardar, actualizar, cambiar estado se implementarán
    // cuando el PedidoService tenga estos métodos disponibles
}
