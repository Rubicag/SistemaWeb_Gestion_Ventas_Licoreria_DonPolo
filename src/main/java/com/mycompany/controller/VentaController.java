package com.mycompany.controller;

import com.mycompany.model.CarritoItem;
import com.mycompany.model.Venta;
import com.mycompany.model.DetalleVenta;
import com.mycompany.service.VentaService;
import com.mycompany.service.UsuarioService;
import com.mycompany.service.ProductoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/ventas")
public class VentaController {
    private final VentaService ventaService;
    private final UsuarioService usuarioService;
    private final ProductoService productoService;

    public VentaController(VentaService ventaService, UsuarioService usuarioService, ProductoService productoService) {
        this.ventaService = ventaService;
        this.usuarioService = usuarioService;
        this.productoService = productoService;
    }

    // Mostrar formulario para registrar venta manual (con productos y método de pago)
    @GetMapping("/registro")
    public String mostrarFormularioVenta(Model model, HttpSession session) {
        model.addAttribute("venta", new Venta());
        model.addAttribute("usuarios", usuarioService.obtenerUsuarios());
        model.addAttribute("productos", productoService.obtenerProductos());
        model.addAttribute("metodosPago", java.util.List.of("Efectivo", "Tarjeta", "Yape", "Plin"));
        // Carrito en sesión
        java.util.List<CarritoItem> carrito = (java.util.List<CarritoItem>) session.getAttribute("carrito");
        if (carrito == null) carrito = new java.util.ArrayList<>();
        model.addAttribute("carrito", carrito);
        return "ventas/nuevo";
    }

    // Procesar registro de venta manual (con detalles y método de pago)
    @PostMapping("/registro")
    public String registrarVenta(@ModelAttribute Venta venta, @RequestParam("metodoPago") String metodoPago, HttpSession session, Model model) {
        java.util.List<CarritoItem> carrito = (java.util.List<CarritoItem>) session.getAttribute("carrito");
        if (carrito == null || carrito.isEmpty()) {
            model.addAttribute("errorMessage", "El carrito está vacío");
            return "ventas/nuevo";
        }
        java.util.List<DetalleVenta> detalles = new java.util.ArrayList<>();
        double total = 0;
        for (CarritoItem item : carrito) {
            DetalleVenta detalle = new DetalleVenta();
            detalle.setProducto(item.getProducto());
            detalle.setCantidad(item.getCantidad());
            detalle.setPrecioUnitario(java.math.BigDecimal.valueOf(item.getProducto().getPrecio()));
            detalle.setSubtotal(java.math.BigDecimal.valueOf(item.getProducto().getPrecio() * item.getCantidad()));
            detalles.add(detalle);
            total += item.getProducto().getPrecio() * item.getCantidad();
        }
        venta.setDetalles(detalles);
        venta.setMetodoPago(metodoPago);
        venta.setTotal(total);
        venta.setFecha(new java.util.Date());
        ventaService.registrarVenta(venta);
        session.removeAttribute("carrito");
        return "redirect:/ventas/listar";
    }

    // Mostrar formulario para editar venta
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable("id") int id, Model model) {
        Venta venta = ventaService.buscarVentaPorId(id);
        model.addAttribute("venta", venta);
    model.addAttribute("usuarios", usuarioService.obtenerUsuarios());
        return "ventas/editar";
    }

    // Procesar actualización de venta
    // Puedes implementar la actualización de ventas si tu lógica lo requiere, pero el método actualizarVenta no existe en el servicio.
    // Si necesitas actualizar una venta, deberás implementar la lógica correspondiente en VentaService y aquí.
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
        var usuario = usuarioService.buscarUsuarioPorCorreo(correo);
        if (usuario == null) {
            model.addAttribute("errorMessage", "No se encontró un usuario asociado al correo actual");
            return "ventas/carrito";
        }
        // Nueva lógica: crear una sola venta con todos los detalles
        Venta venta = new Venta();
        venta.setUsuario(usuario);
        java.util.List<DetalleVenta> detalles = new java.util.ArrayList<>();
        for (CarritoItem item : carrito) {
            DetalleVenta detalle = new DetalleVenta();
            detalle.setProducto(item.getProducto());
            detalle.setCantidad(item.getCantidad());
            detalle.setPrecioUnitario(java.math.BigDecimal.valueOf(item.getProducto().getPrecio()));
            detalle.setSubtotal(java.math.BigDecimal.valueOf(item.getProducto().getPrecio() * item.getCantidad()));
            detalles.add(detalle);
            total += item.getProducto().getPrecio() * item.getCantidad();
        }
        venta.setDetalles(detalles);
        venta.setMetodoPago("Efectivo"); // O puedes obtenerlo de la vista si lo deseas
        venta.setTotal(total);
        venta.setFecha(new java.util.Date());
        ventaService.registrarVenta(venta);
        session.setAttribute("total", total);
        session.setAttribute("carrito", carrito);
    }

    model.addAttribute("successMessage", "Compra realizada con éxito");
    return "ventas/carrito";
}
@GetMapping("/historial")
public String historialCompras(Model model) {
    List<Venta> ventas = ventaService.obtenerVentas();
    model.addAttribute("ventas", ventas);
    return "ventas/listar";
}

}