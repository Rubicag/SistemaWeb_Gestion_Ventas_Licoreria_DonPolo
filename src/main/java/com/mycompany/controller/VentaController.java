package com.mycompany.controller;

import com.mycompany.model.CarritoItem;
import com.mycompany.model.Venta;
import com.mycompany.service.ProductoService;
import com.mycompany.service.VentaService;
import com.mycompany.service.ClienteService;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

@Controller
@RequestMapping("/ventas")
public class VentaController {

    private final VentaService ventaService;
    private final ClienteService clienteService;

    public VentaController(VentaService ventaService, ProductoService productoService, ClienteService clienteService) {
        this.ventaService = ventaService;
        this.clienteService = clienteService;
    }

    // Mostrar formulario para registrar venta manual
    @GetMapping("/registro")
    public String mostrarFormularioVenta(Model model) {
        model.addAttribute("venta", new Venta());
        return "ventas/registro"; // JSP: /WEB-INF/views/ventas/registro.jsp
    }

    // Procesar registro de venta manual
    @PostMapping("/registro")
    public String registrarVenta(@ModelAttribute Venta venta, Model model) {
        ventaService.registrarVenta(venta);
        return "redirect:/ventas/listar";
    }

    // Mostrar formulario para editar venta
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable("id") int id, Model model) {
        Venta venta = ventaService.buscarVentaPorId(id);
        model.addAttribute("venta", venta);
        model.addAttribute("clientes", clienteService.obtenerClientes());
        return "ventas/editar";
    }

    // Procesar actualización de venta
    @PostMapping("/actualizar")
    public String actualizarVenta(@ModelAttribute Venta venta) {
        ventaService.actualizarVenta(venta);
        return "redirect:/ventas/listar";
    }
    // ...existing code...

    // Eliminar producto del carrito
    @GetMapping("/carrito/eliminar/{id}")
    public String eliminarDelCarrito(@PathVariable("id") int id, HttpSession session) {
        List<CarritoItem> carrito = (List<CarritoItem>) session.getAttribute("carrito");
        if (carrito != null) {
            carrito.removeIf(item -> item.getProducto().getIdProducto() == id);
            session.setAttribute("carrito", carrito);
        }
        return "redirect:/ventas/carrito";
    }

    // Mostrar carrito
    @GetMapping("/carrito")
    public String mostrarCarrito(HttpSession session, Model model) {
        List<CarritoItem> carrito = (List<CarritoItem>) session.getAttribute("carrito");
        if (carrito == null) carrito = new ArrayList<>();

        double total = carrito.stream()
                .mapToDouble(item -> item.getProducto().getPrecio() * item.getCantidad())
                .sum();

        model.addAttribute("carrito", carrito);
        model.addAttribute("total", total);
        return "ventas/carrito"; // JSP: /WEB-INF/views/ventas/carrito.jsp
    }

    // Procesar checkout
   /**
 * @param session
 * @param model
 * @return
 */
@PostMapping("/checkout")
public String checkout(HttpSession session, Model model) {
    List<CarritoItem> carrito = (List<CarritoItem>) session.getAttribute("carrito");
    double total = 0;

    if (carrito != null) {
        // Obtener el correo del usuario autenticado
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String correo = auth.getName();
        var cliente = clienteService.buscarClientePorCorreo(correo);
        if (cliente == null) {
            model.addAttribute("errorMessage", "No se encontró un cliente asociado al usuario actual");
            return "ventas/checkout";
        }
        for (CarritoItem item : carrito) {
            Venta venta = new Venta();
            venta.setCliente(cliente);
            venta.setProducto(item.getProducto());
            venta.setCantidad(item.getCantidad());
            venta.setTotal(item.getProducto().getPrecio() * item.getCantidad());
            venta.setFecha(new Date());
            ventaService.registrarVenta(venta);
            total += venta.getTotal();
        }
        session.setAttribute("total", total);
        session.setAttribute("carrito", carrito); // Mantener para mostrar en checkout
        // session.removeAttribute("carrito"); // Opcional: vaciar después de mostrar
    }

    return "ventas/checkout"; // JSP checkout.jsp
}
@GetMapping("/historial")
public String historialCompras(Model model) {
    List<Venta> ventas = ventaService.obtenerVentas();
    model.addAttribute("ventas", ventas);
    return "ventas/listar";
}

}