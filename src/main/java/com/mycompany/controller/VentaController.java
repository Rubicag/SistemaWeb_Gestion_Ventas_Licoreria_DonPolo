package com.mycompany.controller;

import com.mycompany.model.CarritoItem;
import com.mycompany.model.Producto;
import com.mycompany.model.Venta;
import com.mycompany.service.ProductoService;
import com.mycompany.service.VentaService;
import jakarta.servlet.http.HttpSession;
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
    private final ProductoService productoService;

    public VentaController(VentaService ventaService, ProductoService productoService) {
        this.ventaService = ventaService;
        this.productoService = productoService;
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

    // Listar todas las ventas
    @GetMapping("/listar")
    public String listarVentas(Model model) {
        List<Venta> ventas = ventaService.obtenerVentas();
        model.addAttribute("ventas", ventas);
        return "ventas/lista";
    }

    // Agregar un producto al carrito
    @GetMapping("/carrito/agregar/{id}")
    public String agregarAlCarrito(@PathVariable("id") int id, HttpSession session) {
        List<CarritoItem> carrito = (List<CarritoItem>) session.getAttribute("carrito");
        if (carrito == null) {
            carrito = new ArrayList<>();
        }

        Producto producto = productoService.buscarProductoPorId(id);
        boolean existe = false;

        for (CarritoItem item : carrito) {
            if (item.getProducto().getId() == id) {
                item.setCantidad(item.getCantidad() + 1);
                existe = true;
                break;
            }
        }

        if (!existe) {
            carrito.add(new CarritoItem(producto, 1));
        }

        session.setAttribute("carrito", carrito);
        return "redirect:/productos/listar";
    }

    // Eliminar producto del carrito
    @GetMapping("/carrito/eliminar/{id}")
    public String eliminarDelCarrito(@PathVariable("id") int id, HttpSession session) {
        List<CarritoItem> carrito = (List<CarritoItem>) session.getAttribute("carrito");
        if (carrito != null) {
            carrito.removeIf(item -> item.getProducto().getId() == id);
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
   @PostMapping("/checkout")
public String checkout(HttpSession session, Model model) {
    List<CarritoItem> carrito = (List<CarritoItem>) session.getAttribute("carrito");
    double total = 0;

    if (carrito != null) {
        for (CarritoItem item : carrito) {
            Venta venta = new Venta();
            venta.setIdCliente(1); // O el cliente de sesión
            venta.setIdProducto(item.getProducto().getId());
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
    List<Venta> ventas = ventaService.obtenerVentas(); // o filtrar por cliente si hay login
    model.addAttribute("ventas", ventas);
    return "ventas/historial"; // JSP: /WEB-INF/views/ventas/historial.jsp
}

}