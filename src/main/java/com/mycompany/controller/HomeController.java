/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    private MessageSource messageSource;

    @GetMapping("/login")
    public String login(Model model) {
        String loginMessage = messageSource.getMessage("login.success", null, null);
        model.addAttribute("loginMessage", loginMessage);
        return "login";  // Redirige a la vista login.html
    }
}
