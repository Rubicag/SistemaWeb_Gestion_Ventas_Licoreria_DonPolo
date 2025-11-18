package com.mycompany.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;
import com.mycompany.model.Carrito;
import com.mycompany.model.Usuario;
import com.mycompany.repository.CarritoRepository;
import com.mycompany.service.UsuarioService;
import com.mycompany.service.ProductoService;

@Controller
@RequestMapping("/carritos")
public class CarritoController {
    private final CarritoRepository carritoRepository;
    private final UsuarioService usuarioService;
    
    @Autowired
    private ProductoService productoService;

    public CarritoController(CarritoRepository carritoRepository, UsuarioService usuarioService) {
        this.carritoRepository = carritoRepository;
        this.usuarioService = usuarioService;
    }
    
    @GetMapping({"", "/", "/listar"})
    public String listarCarritos(Model model) {
        List<Carrito> carritos = carritoRepository.findAll();
        model.addAttribute("carritos", carritos);
        model.addAttribute("usuarios", usuarioService.obtenerUsuarios());
        model.addAttribute("productos", productoService.listarDisponibles());
        return "carritos";
    }

    @PostMapping("/guardar")
    public String guardarCarrito(@RequestParam("usuario.idUsuario") Integer idUsuario,
                                 @RequestParam("estado") String estado,
                                 RedirectAttributes redirectAttributes) {
        try {
            Usuario usuario = usuarioService.buscarUsuarioPorId(idUsuario);
            if (usuario == null) {
                throw new IllegalArgumentException("Usuario no encontrado");
            }
            Carrito carrito = new Carrito();
            carrito.setUsuario(usuario);
            carrito.setFechaCreacion(new java.util.Date());
            carrito.setEstado(estado);
            carritoRepository.save(carrito);
            redirectAttributes.addFlashAttribute("mensaje", "Carrito creado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/carritos";
    }

    @PostMapping("/agregarProducto")
    public String agregarProducto(@RequestParam("idCarrito") Integer idCarrito,
                                  @RequestParam("idProducto") Integer idProducto,
                                  @RequestParam("cantidad") Integer cantidad,
                                  RedirectAttributes redirectAttributes) {
        try {
            // Lógica para agregar producto al carrito
            redirectAttributes.addFlashAttribute("mensaje", "Producto agregado al carrito");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/carritos";
    }

    @GetMapping("/cambiarEstado/{id}/{nuevoEstado}")
    public String cambiarEstado(@PathVariable Integer id, @PathVariable String nuevoEstado,
                                RedirectAttributes redirectAttributes) {
        try {
            Carrito carrito = carritoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Carrito no encontrado"));
            carrito.setEstado(nuevoEstado);
            carritoRepository.save(carrito);
            redirectAttributes.addFlashAttribute("mensaje", "Estado cambiado a " + nuevoEstado);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/carritos";
    }

    @GetMapping("/vaciar/{id}")
    public String vaciarCarrito(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            // Lógica para vaciar carrito
            redirectAttributes.addFlashAttribute("mensaje", "Carrito vaciado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/carritos";
    }

    @GetMapping("/convertirAVenta/{id}")
    public String convertirAVenta(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            // Lógica para convertir carrito en venta
            redirectAttributes.addFlashAttribute("mensaje", "Carrito convertido a venta exitosamente");
            return "redirect:/ventas";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/carritos";
        }
    }
}
