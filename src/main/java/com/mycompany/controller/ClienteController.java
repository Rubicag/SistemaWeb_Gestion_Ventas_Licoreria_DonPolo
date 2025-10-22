/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.controller;

import com.mycompany.model.Cliente;
import com.mycompany.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/clientes")
public class ClienteController {

	@Autowired
	private ClienteService clienteService;

	@GetMapping("")
	public String listarClientes(Model model) {
		List<Cliente> clientes = clienteService.listarTodos();
		model.addAttribute("clientes", clientes);
		return "clientes/listar";
	}

	@GetMapping("/nuevo")
	public String mostrarFormularioNuevo(Model model) {
		model.addAttribute("cliente", new Cliente());
		return "clientes/registrar";
	}

	@PostMapping("/guardar")
	public String guardarCliente(@ModelAttribute Cliente cliente) {
		clienteService.guardar(cliente);
		return "redirect:/clientes";
	}

	@GetMapping("/editar/{id}")
	public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
		Cliente cliente = clienteService.buscarPorId(id);
		model.addAttribute("cliente", cliente);
		return "clientes/registrar";
	}

	@GetMapping("/eliminar/{id}")
	public String eliminarCliente(@PathVariable Long id) {
		clienteService.eliminar(id);
		return "redirect:/clientes";
	}
}