package com.mycompany.controller;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controlador para la página de login.
 * Mapea GET /login y convierte los parámetros ?error, ?logout, ?expired en mensajes para la vista.
 */
@Controller
public class LoginController {

    @GetMapping("/login")
    public String login(HttpServletRequest request, Model model) {
        String error = request.getParameter("error");
        String logout = request.getParameter("logout");
        String expired = request.getParameter("expired");

        if (error != null) {
            model.addAttribute("errorMessage", "Usuario o contraseña incorrectos. Por favor verifica tus datos.");
        }

        if (expired != null) {
            model.addAttribute("errorMessage", "Tu sesión ha expirado. Por favor inicia sesión de nuevo.");
        }

        if (logout != null) {
            model.addAttribute("successMessage", "Has cerrado sesión correctamente.");
        }

        return "login";
    }

}