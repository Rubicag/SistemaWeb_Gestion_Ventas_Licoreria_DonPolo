package com.mycompany.controller;

import com.mycompany.dto.VentaDTO;
import com.mycompany.service.VentaService;
import com.mycompany.service.ProductoService;
import com.mycompany.service.ClienteService;
import com.mycompany.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigDecimal;
import java.util.List;

/**
 * Controlador principal - Dashboard con estadísticas
 */
@Controller
public class HomeController {

    @Autowired
    private VentaService ventaService;

    @Autowired
    private ProductoService productoService;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping({"/", "/home"})
    public String home(Model model) {
        try {
            // Estadísticas de ventas del día
            BigDecimal ventasDelDia = ventaService.calcularVentasDelDia();
            model.addAttribute("ventasDelDia", ventasDelDia != null ? ventasDelDia : BigDecimal.ZERO);

            // Total de ventas (últimos 30 días)
            java.time.LocalDateTime hace30Dias = java.time.LocalDateTime.now().minusDays(30);
            java.time.LocalDateTime ahora = java.time.LocalDateTime.now();
            BigDecimal totalVentas = ventaService.calcularTotalVentas(hace30Dias, ahora);
            model.addAttribute("totalVentas", totalVentas != null ? totalVentas : BigDecimal.ZERO);

            // Productos con bajo stock (alerta)
            var productosBajoStock = productoService.obtenerProductosConBajoStock();
            model.addAttribute("productosBajoStock", productosBajoStock.size());

            // Estadísticas de clientes activos
            long clientesActivos = clienteService.contarActivos();
            model.addAttribute("clientesActivos", clientesActivos);

            // Estadísticas de usuarios activos
            long usuariosActivos = usuarioService.contarUsuariosActivos();
            model.addAttribute("usuariosActivos", usuariosActivos);

            // Últimas ventas (para mostrar en dashboard) - Convertir a DTO
            List<VentaDTO> ultimasVentas = ventaService.convertirListaADTO(ventaService.obtenerUltimasVentas());
            model.addAttribute("ultimasVentas", ultimasVentas);

        } catch (Exception e) {
            // En caso de error, mostrar valores por defecto
            model.addAttribute("ventasDelDia", BigDecimal.ZERO);
            model.addAttribute("totalVentas", BigDecimal.ZERO);
            model.addAttribute("productosBajoStock", 0);
            model.addAttribute("clientesActivos", 0L);
            model.addAttribute("usuariosActivos", 0L);
        }

        return "home";
    }
}
