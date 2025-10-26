/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.controller;
import com.mycompany.model.Usuario;
import com.mycompany.service.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.crypto.password.PasswordEncoder;
/**
 *
 * @author LUIGGI
 */
@Controller
public class RegistroController {

    private final UsuarioService usuarioService;
    private final PasswordEncoder passwordEncoder;

    public RegistroController(UsuarioService usuarioService, PasswordEncoder passwordEncoder) {
        this.usuarioService = usuarioService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/registro")
    public String mostrarFormulario() {
        return "registro"; // JSP registro.jsp
    }

    @PostMapping("/registro")
    public String registrarUsuario(@RequestParam("nombre") String nombre,
                                   @RequestParam("email") String email,
                                   @RequestParam("password") String password,
                                   @RequestParam("confirmPassword") String confirmPassword,
                                   Model model) {

        if (!password.equals(confirmPassword)) {
            model.addAttribute("errorMessage", "Las contraseñas no coinciden");
            return "registro";
        }

        Usuario usuario = new Usuario();
        usuario.setNombre(nombre);
        usuario.setCorreo(email);
        // Si la contraseña no está encriptada, la encripta (migración automática)
        if (!password.startsWith("$2a$") && !password.startsWith("$2b$") && !password.startsWith("$2y$")) {
            usuario.setContrasena(passwordEncoder.encode(password));
        } else {
            usuario.setContrasena(password);
        }

        String error = usuarioService.registrarUsuario(usuario);

        if (error != null) {
            model.addAttribute("errorMessage", error);
            return "registro";
        }

        return "redirect:/login"; // Redirigir al login después del registro exitoso
    }
}