/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.controller;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
/**
 *
 * @author LUIGGI
 */
@Controller
public class LoginController {

    @GetMapping("/login")
    public String showLoginPage() {
        return "login"; // Nombre de la vista JSP
    }

    @PostMapping("/login")
    public String login(@RequestParam("username") String username,
                        @RequestParam("password") String password, 
                        Model model) {
        // Lógica de validación de usuario (puedes agregar tu propia lógica aquí)
        if ("admin".equals(username) && "admin123".equals(password)) {
            return "redirect:/home";  // Redirige al home después de un login exitoso
        } else {
            model.addAttribute("errorMessage", "Credenciales inválidas");
            return "login";  // Vuelve a la página de login con el mensaje de error
        }
    }
}