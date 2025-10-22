package com.mycompany.controller;

import com.mycompany.model.Venta;
import com.mycompany.service.VentaService;
import com.mycompany.service.ClienteService;
import com.mycompany.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/ventas")
public class VentaController {

	@Autowired
	private VentaService ventaService;

	@Autowired
	private ClienteService clienteService;

	@Autowired
	private ProductoService productoService;

	@GetMapping("")
	public String listarVentas(Model model) {
		List<Venta> ventas = ventaService.listarTodos();
		model.addAttribute("ventas", ventas);
		return "ventas/listar";
	}

	@GetMapping("/nuevo")
	public String mostrarFormularioNuevo(Model model) {
		model.addAttribute("venta", new Venta());
		model.addAttribute("clientes", clienteService.listarTodos());
		model.addAttribute("productos", productoService.listarTodos());
		return "ventas/checkout";
	}

	@PostMapping("/guardar")
	public String guardarVenta(@ModelAttribute Venta venta) {
		ventaService.guardar(venta);
		return "redirect:/ventas";
	}

	@GetMapping("/eliminar/{id}")
	public String eliminarVenta(@PathVariable Long id) {
		ventaService.eliminar(id);
		return "redirect:/ventas";
	}
}
