package com.mycompany.controller;

import com.mycompany.dto.UsuarioSimpleDTO;
import com.mycompany.model.Usuario;
import com.mycompany.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controlador para gestión de Usuarios (empleados/administradores)
 */
@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping({"", "/", "/listar"})
    public String listarUsuarios(Model model) {
        // Convertir usuarios a DTO simple para evitar referencias circulares
        List<UsuarioSimpleDTO> usuariosDTO = usuarioService.obtenerUsuarios().stream()
            .map(u -> new UsuarioSimpleDTO(
                u.getIdUsuario(),
                u.getNombre(),
                u.getCorreo(),
                u.getRol(),
                u.isActivo()
            ))
            .collect(Collectors.toList());
        model.addAttribute("usuarios", usuariosDTO);
        return "usuarios";
    }

    @PostMapping("/guardar")
    public String guardarUsuario(@Valid @ModelAttribute("usuario") Usuario usuario,
                                BindingResult result,
                                Model model,
                                RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            List<UsuarioSimpleDTO> usuariosDTO = usuarioService.obtenerUsuarios().stream()
                .map(u -> new UsuarioSimpleDTO(
                    u.getIdUsuario(), u.getNombre(), u.getCorreo(), u.getRol(), u.isActivo()
                ))
                .collect(Collectors.toList());
            model.addAttribute("usuarios", usuariosDTO);
            return "usuarios";
        }

        try {
            usuarioService.guardarUsuario(usuario);
            redirectAttributes.addFlashAttribute("mensaje", "Usuario creado exitosamente");
            return "redirect:/usuarios";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/usuarios";
        }
    }

    @PostMapping("/actualizar")
    public String actualizarUsuario(@Valid @ModelAttribute("usuario") Usuario usuario,
                                    BindingResult result,
                                    Model model,
                                    RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            List<UsuarioSimpleDTO> usuariosDTO = usuarioService.obtenerUsuarios().stream()
                .map(u -> new UsuarioSimpleDTO(
                    u.getIdUsuario(), u.getNombre(), u.getCorreo(), u.getRol(), u.isActivo()
                ))
                .collect(Collectors.toList());
            model.addAttribute("usuarios", usuariosDTO);
            return "usuarios";
        }

        try {
            usuarioService.actualizarUsuario(usuario);
            redirectAttributes.addFlashAttribute("mensaje", "Usuario actualizado exitosamente");
            return "redirect:/usuarios";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/usuarios";
        }
    }

    @GetMapping("/cambiarEstado/{id}")
    public String cambiarEstado(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            Usuario usuario = usuarioService.buscarUsuarioPorId(id);
            if (usuario == null) {
                throw new IllegalArgumentException("Usuario no encontrado");
            }
            usuario.setActivo(!usuario.isActivo());
            usuarioService.actualizarUsuario(usuario);
            String estado = usuario.isActivo() ? "activado" : "desactivado";
            redirectAttributes.addFlashAttribute("mensaje", "Usuario " + estado + " exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al cambiar estado: " + e.getMessage());
        }
        return "redirect:/usuarios";
    }
}

