package com.mycompany.controller;

import com.mycompany.model.*;
import com.mycompany.service.PedidoService;
import com.mycompany.service.PagoService;
import com.mycompany.repository.PedidoDetalleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;

@Controller
@RequestMapping("/reportes")
public class ReporteController {
	@Autowired
	private PedidoService pedidoService;
	@Autowired
	private PagoService pagoService;
	@Autowired
	private PedidoDetalleRepository pedidoDetalleRepository;

	@GetMapping("/pedidos")
	public String reportePedidos(Model model) {
		List<Pedido> pedidos = pedidoService.listarPedidos();
		model.addAttribute("pedidos", pedidos);
		return "reportes/reportePedidos";
	}

	@GetMapping("/pagos")
	public String reportePagos(Model model) {
		List<Pago> pagos = pagoService.listarPagos();
		model.addAttribute("pagos", pagos);
		return "reportes/reportePagos";
	}

	@GetMapping("/productos-mas-vendidos")
	public String productosMasVendidos(Model model) {
		List<PedidoDetalle> detalles = pedidoDetalleRepository.findAll();
		Map<Producto, Integer> ventasPorProducto = new HashMap<>();
		for (PedidoDetalle detalle : detalles) {
			Producto producto = detalle.getProducto();
			ventasPorProducto.put(producto, ventasPorProducto.getOrDefault(producto, 0) + detalle.getCantidad());
		}
		List<Map.Entry<Producto, Integer>> ranking = new ArrayList<>(ventasPorProducto.entrySet());
		ranking.sort((a, b) -> b.getValue().compareTo(a.getValue()));
		model.addAttribute("ranking", ranking);
		return "reportes/productosMasVendidos";
	}

	@GetMapping("/ventas-por-cliente")
	public String ventasPorCliente(Model model) {
		List<Pedido> pedidos = pedidoService.listarPedidos();
		Map<Cliente, Double> ventasPorCliente = new HashMap<>();
		for (Pedido pedido : pedidos) {
			Cliente cliente = (Cliente) pedido.getCliente();
			ventasPorCliente.put(cliente, ventasPorCliente.getOrDefault(cliente, 0.0) + pedido.getTotal());
		}
		List<Map.Entry<Cliente, Double>> ranking = new ArrayList<>(ventasPorCliente.entrySet());
		ranking.sort((a, b) -> b.getValue().compareTo(a.getValue()));
		model.addAttribute("ranking", ranking);
		return "reportes/ventasPorCliente";
	}
}
