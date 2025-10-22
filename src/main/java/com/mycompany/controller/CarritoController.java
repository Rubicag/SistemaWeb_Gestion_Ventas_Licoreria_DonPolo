
package com.mycompany.controller;

import com.mycompany.model.Carrito;
import com.mycompany.model.CarritoItem;
import com.mycompany.model.Producto;
import com.mycompany.model.Promocion;
import com.mycompany.model.Notificacion;
import com.mycompany.service.PromocionService;
import com.mycompany.service.NotificacionService;
import com.mycompany.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/carrito")
public class CarritoController {
    @Autowired
    private PromocionService promocionService;

    @Autowired
    private NotificacionService notificacionService;

    @Autowired
    private ProductoService productoService;

    @GetMapping("/ver")
    public String verCarrito(HttpSession session, Model model) {
        Carrito carrito = obtenerCarrito(session);
        model.addAttribute("carrito", carrito);
        return "carrito/verCarrito";
    }

    @GetMapping("/agregar/{id}")
    public String agregarAlCarrito(@PathVariable Long id, @RequestParam(defaultValue = "1") int cantidad, HttpSession session) {
        Producto producto = productoService.buscarPorId(id);
        if (producto != null) {
            Carrito carrito = obtenerCarrito(session);
            // Aplicar promoción si existe
            Promocion promo = promocionService.buscarPromocionActivaPorProducto(producto.getId() != null ? producto.getId().longValue() : null);
            Producto productoConDescuento = producto;
            if (promo != null) {
                productoConDescuento = new Producto();
                productoConDescuento.setId(producto.getId());
                productoConDescuento.setNombre(producto.getNombre());
                productoConDescuento.setPrecio(producto.getPrecio() * (1 - promo.getDescuento()));
                productoConDescuento.setCategoria(producto.getCategoria());
                productoConDescuento.setCantidad(producto.getCantidad());
            }
            carrito.agregarItem(new CarritoItem(productoConDescuento, cantidad));
            session.setAttribute("carrito", carrito);
            // Notificación automática si el stock es bajo
            int stockRestante = producto.getCantidad() - cantidad;
            if (stockRestante <= 3) {
                String mensaje = "Stock bajo para producto: " + producto.getNombre() + ", quedan: " + stockRestante;
                notificacionService.guardar(new Notificacion(mensaje, new java.util.Date(), false));
            }
        }
        return "redirect:/carrito/ver";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarDelCarrito(@PathVariable Integer id, HttpSession session) {
        Carrito carrito = obtenerCarrito(session);
        carrito.eliminarItem(id);
        session.setAttribute("carrito", carrito);
        return "redirect:/carrito/ver";
    }

    @GetMapping("/vaciar")
    public String vaciarCarrito(HttpSession session) {
        Carrito carrito = obtenerCarrito(session);
        carrito.vaciar();
        session.setAttribute("carrito", carrito);
        return "redirect:/carrito/ver";
    }

    private Carrito obtenerCarrito(HttpSession session) {
        Carrito carrito = (Carrito) session.getAttribute("carrito");
        if (carrito == null) {
            carrito = new Carrito();
            session.setAttribute("carrito", carrito);
        }
        return carrito;
    }
}
