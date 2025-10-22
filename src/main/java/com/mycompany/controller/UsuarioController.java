/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mycompany.controller;

import com.mycompany.model.Usuario;
import com.mycompany.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

	@Autowired
	private UsuarioService usuarioService;

	@GetMapping
	public String listarUsuarios(Model model) {
		model.addAttribute("usuarios", usuarioService.listarUsuarios());
		return "usuarios/lista";
	}

	@GetMapping("/nuevo")
	public String mostrarFormularioRegistro(Model model) {
		model.addAttribute("usuario", new Usuario());
		return "usuarios/formulario";
	}

	@PostMapping("/guardar")
	public String guardarUsuario(@ModelAttribute Usuario usuario) {
		usuarioService.guardarUsuario(usuario);
		return "redirect:/usuarios";
	}

	@GetMapping("/editar/{id}")
	public String mostrarFormularioEditar(@PathVariable Integer id, Model model) {
		Optional<Usuario> usuario = usuarioService.buscarPorId(id);
		if (usuario.isPresent()) {
			model.addAttribute("usuario", usuario.get());
			return "usuarios/formulario";
		} else {
			return "redirect:/usuarios";
		}
	}

	@GetMapping("/eliminar/{id}")
	public String eliminarUsuario(@PathVariable Integer id) {
		usuarioService.eliminarUsuario(id);
		return "redirect:/usuarios";
	}

	// Login básico (solo vista, lógica real con Spring Security)
	@GetMapping("/login")
	public String mostrarLogin() {
		return "usuarios/login";
	}
}
