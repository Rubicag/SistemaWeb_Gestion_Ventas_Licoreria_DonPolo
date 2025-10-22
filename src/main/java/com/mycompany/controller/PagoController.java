
package com.mycompany.controller;


import com.mycompany.model.Pago;
import com.mycompany.model.Pedido;
import com.mycompany.model.Cliente;
import com.mycompany.model.Notificacion;
import com.mycompany.service.PagoService;
import com.mycompany.service.PedidoService;
import com.mycompany.service.ClienteService;
import com.mycompany.service.NotificacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/pagos")
public class PagoController {
	@Autowired
	private NotificacionService notificacionService;
	@Autowired
	private PagoService pagoService;
	@Autowired
	private PedidoService pedidoService;
	@Autowired
	private ClienteService clienteService;

	@GetMapping("/registrar")
	public String mostrarFormularioPago(Model model) {
		List<Pedido> pedidos = pedidoService.listarPedidos();
		List<Cliente> clientes = clienteService.listarTodos();
		model.addAttribute("pedidos", pedidos);
		model.addAttribute("clientes", clientes);
		return "pagos/registrarPago";
	}

	@PostMapping("/registrar")
	public String registrarPago(@RequestParam Long pedidoId,
								@RequestParam Long clienteId,
								@RequestParam double monto,
								@RequestParam String metodo,
								Model model) {
		Pedido pedido = pedidoService.buscarPorId(pedidoId);
		Cliente cliente = clienteService.buscarPorId(clienteId);
		if (pedido == null || cliente == null) {
			model.addAttribute("error", "Pedido o cliente no válido.");
			return "pagos/registrarPago";
		}
		Pago pago = new Pago();
		pago.setPedido(pedido);
		pago.setCliente(cliente);
		pago.setMonto(monto);
		pago.setMetodo(metodo);
		pago.setFecha(new Date());
	pagoService.guardarPago(pago);
	// Notificación automática
	String mensaje = "Pago registrado para cliente: " + cliente.getNombre() + ", pedido: " + pedido.getId() + ", monto: S/ " + monto;
	notificacionService.guardar(new Notificacion(mensaje, new java.util.Date(), false));
	model.addAttribute("mensaje", "Pago registrado correctamente.");
	return "pagos/pagoExito";
	}

	@GetMapping("/listar")
	public String listarPagos(Model model) {
		List<Pago> pagos = pagoService.listarPagos();
		model.addAttribute("pagos", pagos);
		return "pagos/listarPagos";
	}
}
