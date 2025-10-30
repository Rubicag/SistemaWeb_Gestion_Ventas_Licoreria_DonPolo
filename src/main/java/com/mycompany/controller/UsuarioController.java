package com.mycompany.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    @GetMapping("/listar")
    public String listarUsuarios(Model model) {
        // model.addAttribute("usuarios", usuarioService.findAll());
        return "usuarios/listar";
    }

    @GetMapping("/nuevo")
    public String nuevoUsuario(Model model) {
        // model.addAttribute("usuario", new Usuario());
        return "usuarios/nuevo";
    }

    @GetMapping("/editar")
    public String editarUsuario(Model model) {
        // model.addAttribute("usuario", usuarioService.findById(id));
        return "usuarios/editar";
    }
}
