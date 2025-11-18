package com.mycompany.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.mycompany.service.ReporteService;
import com.mycompany.service.VentaService;
import com.mycompany.service.ProductoService;
import com.mycompany.service.ClienteService;
import com.mycompany.service.ProveedorService;
import com.mycompany.service.PromocionService;
import com.mycompany.model.ReporteVentaProducto;
import com.mycompany.dto.VentaReporteDTO;
import com.mycompany.dto.ClienteReporteDTO;
import com.mycompany.dto.ProveedorReporteDTO;
import com.mycompany.dto.PromocionReporteDTO;
import java.util.List;
import java.util.stream.Collectors;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Controller
@RequestMapping("/reportes")
public class ReportesController {
    private final ReporteService reporteService;
    
    @Autowired
    private VentaService ventaService;
    
    @Autowired
    private ProductoService productoService;
    
    @Autowired
    private ClienteService clienteService;
    
    @Autowired
    private ProveedorService proveedorService;
    
    @Autowired
    private PromocionService promocionService;

    public ReportesController(ReporteService reporteService) {
        this.reporteService = reporteService;
    }

    @GetMapping({"", "/", "/listar"})
    public String listarReportes(Model model) {
        // Estadísticas rápidas
        BigDecimal totalVentasHoy = ventaService.calcularVentasDelDia();
        model.addAttribute("totalVentasHoy", totalVentasHoy != null ? totalVentasHoy : BigDecimal.ZERO);
        
        java.time.LocalDateTime hace30Dias = java.time.LocalDateTime.now().minusDays(30);
        java.time.LocalDateTime ahora = java.time.LocalDateTime.now();
        long ventasMes = ventaService.contarVentas(hace30Dias, ahora);
        model.addAttribute("ventasMes", ventasMes);
        
        long totalClientes = clienteService.contarActivos();
        model.addAttribute("totalClientes", totalClientes);
        
        var productosBajoStock = productoService.obtenerProductosConBajoStock();
        model.addAttribute("stockBajo", productosBajoStock.size());
        
        // Reportes disponibles
        List<ReporteVentaProducto> reporteVentas = reporteService.obtenerReporteVentasPorProducto();
        model.addAttribute("reporteVentas", reporteVentas);
        
        // Obtener todas las ventas para el reporte - convertir a DTO para evitar referencias circulares
        var todasLasVentas = ventaService.listarTodas();
        List<VentaReporteDTO> ventasDTO = todasLasVentas.stream()
            .map(venta -> new VentaReporteDTO(
                venta.getIdVenta(),
                venta.getFecha(),
                venta.getCliente() != null ? venta.getCliente().getNombre() : "Público General",
                venta.getUsuario() != null ? venta.getUsuario().getNombre() : "N/A",
                venta.getMetodoPago(),
                venta.getTotal(),
                venta.getEstado()
            ))
            .collect(Collectors.toList());
        model.addAttribute("todasLasVentas", ventasDTO);
        
        // Obtener clientes con estadísticas de compras
        var todosLosClientes = clienteService.listarActivos();
        List<ClienteReporteDTO> clientesDTO = todosLosClientes.stream()
            .map(cliente -> {
                // Contar compras del cliente
                long totalCompras = todasLasVentas.stream()
                    .filter(v -> v.getCliente() != null && v.getCliente().getIdCliente().equals(cliente.getIdCliente()))
                    .count();
                
                // Calcular monto total gastado
                BigDecimal montoTotal = todasLasVentas.stream()
                    .filter(v -> v.getCliente() != null && v.getCliente().getIdCliente().equals(cliente.getIdCliente()))
                    .map(v -> v.getTotal())
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
                
                return new ClienteReporteDTO(
                    cliente.getIdCliente(),
                    cliente.getNombre(),
                    cliente.getApellido(),
                    cliente.getDni(),
                    cliente.getEmail(),
                    cliente.getTelefono(),
                    totalCompras,
                    montoTotal,
                    cliente.getEstado()
                );
            })
            .collect(Collectors.toList());
        model.addAttribute("todosLosClientes", clientesDTO);
        
        // Obtener proveedores con estadísticas de productos
        var todosLosProveedores = proveedorService.listarTodos();
        List<ProveedorReporteDTO> proveedoresDTO = todosLosProveedores.stream()
            .map(proveedor -> new ProveedorReporteDTO(
                proveedor.getIdProveedor(),
                proveedor.getNombre(),
                proveedor.getRuc(),
                proveedor.getEmail(),
                proveedor.getTelefono(),
                proveedor.getContacto(),
                proveedor.getCantidadProductos(),
                proveedor.getEstado()
            ))
            .collect(Collectors.toList());
        model.addAttribute("todosLosProveedores", proveedoresDTO);
        
        // Obtener promociones con estadísticas
        var todasLasPromociones = promocionService.listarTodas();
        List<PromocionReporteDTO> promocionesDTO = todasLasPromociones.stream()
            .map(promo -> {
                String nombreProducto = promo.getProducto() != null ? promo.getProducto().getNombre() : "N/A";
                BigDecimal precioOriginal = promo.getProducto() != null ? promo.getProducto().getPrecio() : BigDecimal.ZERO;
                BigDecimal precioConDescuento = promo.calcularPrecioConDescuento(precioOriginal);
                boolean vigente = promo.isVigente();
                long diasRestantes = promo.getDiasRestantes();
                String estado = promo.isActivo() ? "ACTIVA" : "INACTIVA";
                
                return new PromocionReporteDTO(
                    promo.getIdPromocion(),
                    promo.getNombre(),
                    promo.getDescripcion(),
                    promo.getDescuento(),
                    promo.getFechaInicio(),
                    promo.getFechaFin(),
                    nombreProducto,
                    precioOriginal,
                    precioConDescuento,
                    vigente,
                    diasRestantes,
                    estado
                );
            })
            .collect(Collectors.toList());
        model.addAttribute("todasLasPromociones", promocionesDTO);
        
        return "reportes";
    }
    
    /**
     * Exporta el reporte de ventas por producto a Excel
     * @return Archivo Excel con el reporte
     */
    @GetMapping("/exportar/ventas-excel")
    public ResponseEntity<byte[]> exportarVentasExcel() {
        try {
            List<ReporteVentaProducto> reportes = reporteService.obtenerReporteVentasPorProducto();
            byte[] excelBytes = reporteService.exportarReporteVentasExcel(reportes);
            
            // Generar nombre de archivo con fecha actual
            String fecha = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            String fileName = "Reporte_Ventas_" + fecha + ".xlsx";
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", fileName);
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
            
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(excelBytes);
                    
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
