/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.controller;

import com.mycompany.model.*;
import com.mycompany.service.PedidoService;
import com.mycompany.service.ClienteService;
import com.mycompany.service.NotificacionService;
import com.mycompany.model.Notificacion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/pedido")
public class PedidoController {
	@Autowired
	private NotificacionService notificacionService;
	@Autowired
	private PedidoService pedidoService;
	@Autowired
	private ClienteService clienteService;

	@GetMapping("/confirmar")
	public String mostrarConfirmacion(HttpSession session, Model model) {
		Carrito carrito = (Carrito) session.getAttribute("carrito");
		if (carrito == null || carrito.getItems().isEmpty()) {
			return "redirect:/carrito/ver";
		}
		model.addAttribute("carrito", carrito);
		// Aquí podrías agregar lógica para obtener el cliente autenticado
		return "pedido/confirmarPedido";
	}

	@PostMapping("/procesar")
	public String procesarPedido(HttpSession session, Model model) {
		Carrito carrito = (Carrito) session.getAttribute("carrito");
		if (carrito == null || carrito.getItems().isEmpty()) {
			return "redirect:/carrito/ver";
		}
		// Simulación: obtener cliente (en un sistema real, sería el usuario autenticado)
	Cliente cliente = clienteService.listarTodos().stream().findFirst().orElse(null);
		if (cliente == null) {
			model.addAttribute("error", "Debe haber al menos un cliente registrado.");
			return "pedido/confirmarPedido";
		}
		Pedido pedido = new Pedido();
		pedido.setCliente(cliente);
		pedido.setFecha(new Date());
		pedido.setTotal(carrito.getTotal());
		List<PedidoDetalle> detalles = new ArrayList<>();
		for (CarritoItem item : carrito.getItems()) {
			PedidoDetalle detalle = new PedidoDetalle();
			detalle.setPedido(pedido);
			detalle.setProducto(item.getProducto());
			detalle.setCantidad(item.getCantidad());
			detalle.setSubtotal(item.getSubtotal());
			detalles.add(detalle);
		}
		pedido.setDetalles(detalles);
	pedidoService.guardarPedido(pedido);
	// Crear notificación automática
	String mensaje = "Pedido confirmado para cliente: " + cliente.getNombre() + ", total: S/ " + carrito.getTotal();
	notificacionService.guardar(new Notificacion(mensaje, new java.util.Date(), false));
	// Limpiar carrito
	carrito.vaciar();
	session.setAttribute("carrito", carrito);
	model.addAttribute("mensaje", "¡Pedido realizado con éxito!");
	return "pedido/pedidoExito";
	}
}
