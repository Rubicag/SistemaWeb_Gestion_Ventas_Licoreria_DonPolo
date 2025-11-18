package com.mycompany.controller;

import com.mycompany.dto.VentaDTO;
import com.mycompany.dto.DetalleVentaDTO;
import com.mycompany.dto.ProductoSimpleDTO;
import com.mycompany.dto.ClienteSimpleDTO;
import com.mycompany.model.Venta;
import com.mycompany.service.VentaService;
import com.mycompany.service.ClienteService;
import com.mycompany.service.ProductoService;
import com.mycompany.service.UsuarioService;
import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Controlador para gestión de Ventas
 */
@Controller
@RequestMapping("/ventas")
public class VentaController {
    private static final Logger logger = LoggerFactory.getLogger(VentaController.class);
    
    @Autowired
    private VentaService ventaService;
    
    @Autowired
    private ClienteService clienteService;
    
    @Autowired
    private ProductoService productoService;

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping({"", "/", "/listar"})
    public String listarVentas(Model model) {
        List<VentaDTO> ventas = ventaService.convertirListaADTO(ventaService.listarTodas());
        model.addAttribute("ventas", ventas);
        
        // Convertir clientes a DTO simple para evitar referencias circulares
        List<ClienteSimpleDTO> clientesDTO = clienteService.listarActivos().stream()
            .map(c -> new ClienteSimpleDTO(
                c.getIdCliente(),
                c.getNombre(),
                c.getApellido(),
                c.getDni(),
                c.getEmail(),
                c.getTelefono(),
                c.getDireccion()
            ))
            .collect(java.util.stream.Collectors.toList());
        model.addAttribute("clientes", clientesDTO);
        
        // Convertir productos a DTO simple para evitar referencias circulares
        List<ProductoSimpleDTO> productosDTO = productoService.listarDisponibles().stream()
            .map(p -> new ProductoSimpleDTO(
                p.getIdProducto(),
                p.getNombre(),
                p.getDescripcion(),
                p.getPrecio(),
                p.getStock(),
                p.getCategoria() != null ? p.getCategoria().getNombre() : "Sin categoría",
                p.getProveedor() != null ? p.getProveedor().getNombre() : "Sin proveedor",
                p.isActivo()
            ))
            .collect(java.util.stream.Collectors.toList());
        model.addAttribute("productos", productosDTO);
        
        model.addAttribute("ventasDelDia", ventaService.calcularVentasDelDia());
        return "ventas";
    }

    @PostMapping("/guardar")
    public String guardarVenta(@ModelAttribute("venta") VentaDTO ventaDTO,
                              Authentication authentication,
                              Model model,
                              RedirectAttributes redirectAttributes) {
        logger.info("POST /ventas/guardar recibido - ventaDTO.idUsuario={}, detalles.size={}, total={}",
            ventaDTO.getIdUsuario(), ventaDTO.getDetalles() != null ? ventaDTO.getDetalles().size() : 0, ventaDTO.getTotal());
        // Si no viene idUsuario, intentar asignarlo desde el usuario autenticado
        try {
            if ((ventaDTO.getIdUsuario() == null || ventaDTO.getIdUsuario() == 0) && authentication != null) {
                String principalName = authentication.getName();
                if (principalName != null && !principalName.isBlank()) {
                    var u = usuarioService.buscarUsuarioPorCorreo(principalName);
                    if (u != null) {
                        ventaDTO.setIdUsuario(u.getIdUsuario());
                    }
                }
            }
        } catch (Exception ex) {
            // fallthrough: VentaService validará la presencia de usuario
        }

        // Calcular total a partir de detalles si no viene
        if (ventaDTO.getTotal() == null || ventaDTO.getTotal().compareTo(BigDecimal.ZERO) == 0) {
            BigDecimal totalCalc = BigDecimal.ZERO;
            if (ventaDTO.getDetalles() != null) {
                for (DetalleVentaDTO d : ventaDTO.getDetalles()) {
                    BigDecimal precio = d.getPrecioUnitario() != null ? d.getPrecioUnitario() : BigDecimal.ZERO;
                    int cantidad = d.getCantidad() != null ? d.getCantidad() : 0;
                    totalCalc = totalCalc.add(precio.multiply(BigDecimal.valueOf(cantidad)));
                }
            }
            ventaDTO.setTotal(totalCalc);
        }

        // Validaciones mínimas
        if (ventaDTO.getMetodoPago() == null || ventaDTO.getMetodoPago().isBlank()) {
            redirectAttributes.addFlashAttribute("error", "Seleccione un método de pago");
            return "redirect:/ventas";
        }
        if (ventaDTO.getDetalles() == null || ventaDTO.getDetalles().isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "La venta debe tener al menos un detalle");
            return "redirect:/ventas";
        }

        try {
            Venta venta = ventaService.crearVenta(ventaDTO);
            redirectAttributes.addFlashAttribute("mensaje", "Venta registrada exitosamente. Total: S/. " + venta.getTotal());
            return "redirect:/ventas";
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", "Error de stock: " + e.getMessage());
            return "redirect:/ventas";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/ventas";
        }
    }

    @GetMapping("/detalle/{id}")
    public String detalleVenta(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Venta venta = ventaService.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Venta no encontrada"));
            model.addAttribute("venta", ventaService.convertirADTO(venta));
            model.addAttribute("ventas", ventaService.convertirListaADTO(ventaService.listarTodas()));
            
            List<ClienteSimpleDTO> clientesDTO = clienteService.listarActivos().stream()
                .map(c -> new ClienteSimpleDTO(
                    c.getIdCliente(), c.getNombre(), c.getApellido(), c.getDni(),
                    c.getEmail(), c.getTelefono(), c.getDireccion()
                ))
                .collect(java.util.stream.Collectors.toList());
            model.addAttribute("clientes", clientesDTO);
            
            List<ProductoSimpleDTO> productosDTO = productoService.listarDisponibles().stream()
                .map(p -> new ProductoSimpleDTO(
                    p.getIdProducto(), p.getNombre(), p.getDescripcion(),
                    p.getPrecio(), p.getStock(),
                    p.getCategoria() != null ? p.getCategoria().getNombre() : "Sin categoría",
                    p.getProveedor() != null ? p.getProveedor().getNombre() : "Sin proveedor",
                    p.isActivo()
                ))
                .collect(java.util.stream.Collectors.toList());
            model.addAttribute("productos", productosDTO);
            return "ventas";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/ventas";
        }
    }

    @GetMapping("/anular/{id}")
    public String anularVenta(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            ventaService.anularVenta(id);
            redirectAttributes.addFlashAttribute("mensaje", "Venta anulada exitosamente");
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", "Venta no encontrada");
        }
        return "redirect:/ventas";
    }

    @GetMapping("/ultimas")
    public String ultimasVentas(Model model) {
        List<VentaDTO> ventas = ventaService.convertirListaADTO(ventaService.obtenerUltimasVentas());
        model.addAttribute("ventas", ventas);
        
        List<ClienteSimpleDTO> clientesDTO = clienteService.listarActivos().stream()
            .map(c -> new ClienteSimpleDTO(
                c.getIdCliente(), c.getNombre(), c.getApellido(), c.getDni(),
                    c.getEmail(), c.getTelefono(), c.getDireccion()
            ))
            .collect(java.util.stream.Collectors.toList());
        model.addAttribute("clientes", clientesDTO);
        
        List<ProductoSimpleDTO> productosDTO = productoService.listarDisponibles().stream()
            .map(p -> new ProductoSimpleDTO(
                p.getIdProducto(), p.getNombre(), p.getDescripcion(),
                p.getPrecio(), p.getStock(),
                p.getCategoria() != null ? p.getCategoria().getNombre() : "Sin categoría",
                p.getProveedor() != null ? p.getProveedor().getNombre() : "Sin proveedor",
                p.isActivo()
            ))
            .collect(java.util.stream.Collectors.toList());
        model.addAttribute("productos", productosDTO);
        return "ventas";
    }

    @GetMapping("/cliente/{idCliente}")
    public String ventasPorCliente(@PathVariable Integer idCliente, Model model, RedirectAttributes redirectAttributes) {
        try {
            List<Venta> ventas = ventaService.listarPorCliente(idCliente);
            model.addAttribute("ventas", ventaService.convertirListaADTO(ventas));
            
            List<ClienteSimpleDTO> clientesDTO = clienteService.listarActivos().stream()
                .map(c -> new ClienteSimpleDTO(
                    c.getIdCliente(), c.getNombre(), c.getApellido(), c.getDni(),
                    c.getEmail(), c.getTelefono(), c.getDireccion()
                ))
                .collect(java.util.stream.Collectors.toList());
            model.addAttribute("clientes", clientesDTO);
            
            List<ProductoSimpleDTO> productosDTO = productoService.listarDisponibles().stream()
                .map(p -> new ProductoSimpleDTO(
                    p.getIdProducto(), p.getNombre(), p.getDescripcion(),
                    p.getPrecio(), p.getStock(),
                    p.getCategoria() != null ? p.getCategoria().getNombre() : "Sin categoría",
                    p.getProveedor() != null ? p.getProveedor().getNombre() : "Sin proveedor",
                    p.isActivo()
                ))
                .collect(java.util.stream.Collectors.toList());
            model.addAttribute("productos", productosDTO);
            return "ventas";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/ventas";
        }
    }

    @GetMapping("/hoy")
    public String ventasDelDia(Model model) {
        LocalDateTime hoy = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime finDia = hoy.plusDays(1);
        List<Venta> ventas = ventaService.listarPorRangoFechas(hoy, finDia);
        model.addAttribute("ventas", ventaService.convertirListaADTO(ventas));
        
        List<ClienteSimpleDTO> clientesDTO = clienteService.listarActivos().stream()
            .map(c -> new ClienteSimpleDTO(
                c.getIdCliente(), c.getNombre(), c.getApellido(), c.getDni(),
                    c.getEmail(), c.getTelefono(), c.getDireccion()
            ))
            .collect(java.util.stream.Collectors.toList());
        model.addAttribute("clientes", clientesDTO);
        
        List<ProductoSimpleDTO> productosDTO = productoService.listarDisponibles().stream()
            .map(p -> new ProductoSimpleDTO(
                p.getIdProducto(), p.getNombre(), p.getDescripcion(),
                p.getPrecio(), p.getStock(),
                p.getCategoria() != null ? p.getCategoria().getNombre() : "Sin categoría",
                p.getProveedor() != null ? p.getProveedor().getNombre() : "Sin proveedor",
                p.isActivo()
            ))
            .collect(java.util.stream.Collectors.toList());
        model.addAttribute("productos", productosDTO);
        model.addAttribute("totalDelDia", ventaService.calcularVentasDelDia());
        return "ventas";
    }
}


