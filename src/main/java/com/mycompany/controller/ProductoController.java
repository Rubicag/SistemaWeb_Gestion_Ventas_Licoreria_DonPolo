/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mycompany.controller;

import com.mycompany.model.Producto;
import com.mycompany.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/productos")
public class ProductoController {

	@Autowired
	private ProductoService productoService;

	@GetMapping("")
	public String listarProductos(Model model) {
		List<Producto> productos = productoService.listarTodos();
		model.addAttribute("productos", productos);
		return "productos/lista";
	}

	@GetMapping("/nuevo")
	public String mostrarFormularioNuevo(Model model) {
		model.addAttribute("producto", new Producto());
		return "productos/nuevo";
	}

	@PostMapping("/guardar")
	public String guardarProducto(@ModelAttribute Producto producto) {
		productoService.guardar(producto);
		return "redirect:/productos";
	}

	@GetMapping("/editar/{id}")
	public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
		Producto producto = productoService.buscarPorId(id);
		model.addAttribute("producto", producto);
		return "productos/editar";
	}

	@PostMapping("/actualizar")
	public String actualizarProducto(@ModelAttribute Producto producto) {
		productoService.actualizar(producto);
		return "redirect:/productos";
	}

	@GetMapping("/eliminar/{id}")
	public String eliminarProducto(@PathVariable Long id) {
		productoService.eliminar(id);
		return "redirect:/productos";
	}
}