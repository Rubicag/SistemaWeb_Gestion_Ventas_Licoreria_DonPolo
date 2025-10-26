/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.controller;
import com.mycompany.model.Cliente;
import com.mycompany.service.ClienteService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
/**
 *
 * @author LUIGGI
 */
@Controller
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    // Mostrar el formulario de registro de cliente
    @GetMapping("/registro")
    public String mostrarFormularioRegistro(Model model) {
        model.addAttribute("cliente", new Cliente());
        return "clientes/registro"; // JSP: /WEB-INF/views/clientes/registro.jsp
    }

    // Procesar el registro de un cliente
    @PostMapping("/registro")
    public String registrarCliente(@ModelAttribute Cliente cliente, Model model) {
        clienteService.registrarCliente(cliente);
        return "redirect:/clientes/listar"; // Redirige a la lista de clientes
    }

    // Listar todos los clientes
    @GetMapping("/listar")
    public String listarClientes(Model model) {
        List<Cliente> clientes = clienteService.obtenerClientes();
        model.addAttribute("clientes", clientes);
        return "clientes/listar";
    }

    // Mostrar formulario de edición
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable("id") int id, Model model) {
        Cliente cliente = clienteService.buscarClientePorId(id);
        if (cliente == null) {
            return "redirect:/clientes/listar";
        }
        model.addAttribute("cliente", cliente);
        return "clientes/editar";
    }

    // Procesar edición
    @PostMapping("/actualizar")
    public String actualizarCliente(@ModelAttribute Cliente cliente) {
        clienteService.actualizarCliente(cliente);
        return "redirect:/clientes/listar";
    }

    // Eliminar cliente
    @GetMapping("/eliminar/{id}")
    public String eliminarCliente(@PathVariable("id") int id) {
        clienteService.eliminarCliente(id);
        return "redirect:/clientes/listar";
    }
}