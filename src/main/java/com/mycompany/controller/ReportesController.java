package com.mycompany.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mycompany.service.ReporteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger logger = LoggerFactory.getLogger(ReportesController.class);
    
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

    /**
     * Endpoint API para obtener datos de reportes filtrados por fecha y tipo (JSON)
     */
    @GetMapping(path = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> apiReportes(@RequestParam String tipo,
                                        @RequestParam(required = false) String fechaInicio,
                                        @RequestParam(required = false) String fechaFin) {
        try {
            java.time.format.DateTimeFormatter df = java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;
            final java.time.LocalDateTime start = (fechaInicio != null && !fechaInicio.isBlank())
                    ? java.time.LocalDate.parse(fechaInicio, df).atStartOfDay()
                    : null;
            final java.time.LocalDateTime end = (fechaFin != null && !fechaFin.isBlank())
                    ? java.time.LocalDate.parse(fechaFin, df).atTime(23, 59, 59)
                    : null;

            switch (tipo) {
                case "ventas": {
                    var todas = ventaService.listarTodas();
                    var filtered = todas.stream().filter(v -> {
                        if (v.getFecha() == null) return false;
                        if (start != null && v.getFecha().isBefore(start)) return false;
                        if (end != null && v.getFecha().isAfter(end)) return false;
                        return true;
                    }).map(v -> new VentaReporteDTO(
                            v.getIdVenta(),
                            v.getFecha(),
                            v.getCliente() != null ? v.getCliente().getNombre() : "Público General",
                            v.getUsuario() != null ? v.getUsuario().getNombre() : "N/A",
                            v.getMetodoPago(),
                            v.getTotal(),
                            v.getEstado()
                    )).collect(Collectors.toList());
                    return ResponseEntity.ok(filtered);
                }
                case "financiero": {
                    var todas = ventaService.listarTodas();
                    var filtered = todas.stream().filter(v -> {
                        if (v.getFecha() == null) return false;
                        if (start != null && v.getFecha().isBefore(start)) return false;
                        if (end != null && v.getFecha().isAfter(end)) return false;
                        return true;
                    }).collect(Collectors.toList());
                    // Agrupar por fecha (yyyy-MM-dd)
                    java.util.Map<String, java.util.Map<String, Object>> agrupado = new java.util.TreeMap<>();
                    java.time.format.DateTimeFormatter outFmt = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    for (var v : filtered) {
                        String f = v.getFecha().format(outFmt);
                        var row = agrupado.computeIfAbsent(f, k -> new java.util.HashMap<>());
                        row.putIfAbsent("fecha", f);
                        row.put("count", ((Integer) row.getOrDefault("count", 0)) + 1);
                        java.math.BigDecimal prev = (java.math.BigDecimal) row.getOrDefault("total", java.math.BigDecimal.ZERO);
                        row.put("total", prev.add(v.getTotal() == null ? java.math.BigDecimal.ZERO : v.getTotal()));
                    }
                    java.util.List<java.util.Map<String, Object>> out = new java.util.ArrayList<>();
                    for (var e : agrupado.values()) {
                        out.add(e);
                    }
                    return ResponseEntity.ok(out);
                }
                case "productos": {
                    var reportes = reporteService.obtenerReporteVentasPorProducto();
                    return ResponseEntity.ok(reportes);
                }
                case "clientes": {
                    var todosLosClientes = clienteService.listarActivos();
                    List<ClienteReporteDTO> clientesDTO = todosLosClientes.stream()
                            .map(cliente -> {
                                long totalCompras = ventaService.listarTodas().stream()
                                        .filter(v -> v.getCliente() != null && v.getCliente().getIdCliente().equals(cliente.getIdCliente()))
                                        .count();
                                java.math.BigDecimal montoTotal = ventaService.listarTodas().stream()
                                        .filter(v -> v.getCliente() != null && v.getCliente().getIdCliente().equals(cliente.getIdCliente()))
                                        .map(v -> v.getTotal())
                                        .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);
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
                            }).collect(Collectors.toList());
                    return ResponseEntity.ok(clientesDTO);
                }
                case "proveedores": {
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
                            )).collect(Collectors.toList());
                    return ResponseEntity.ok(proveedoresDTO);
                }
                case "promociones": {
                    var todasLasPromociones = promocionService.listarTodas();
                    List<PromocionReporteDTO> promocionesDTO = todasLasPromociones.stream()
                            .map(promo -> {
                                String nombreProducto = promo.getProducto() != null ? promo.getProducto().getNombre() : "N/A";
                                java.math.BigDecimal precioOriginal = promo.getProducto() != null ? promo.getProducto().getPrecio() : java.math.BigDecimal.ZERO;
                                java.math.BigDecimal precioConDescuento = promo.calcularPrecioConDescuento(precioOriginal);
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
                            }).collect(Collectors.toList());
                    return ResponseEntity.ok(promocionesDTO);
                }
                default:
                    return ResponseEntity.badRequest().body(java.util.Map.of("error", "tipo de reporte desconocido"));
            }
        } catch (Exception e) {
            logger.error("Error en apiReportes", e);
            return ResponseEntity.internalServerError().body(java.util.Map.of("error", "error interno"));
        }
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
            logger.error("Error al exportar reporte de ventas a Excel", e);
            return ResponseEntity.internalServerError().build();
        }
    }
}
