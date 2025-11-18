package com.mycompany.service;

import com.mycompany.dto.VentaDTO;
import com.mycompany.dto.DetalleVentaDTO;
import com.mycompany.model.*;
import com.mycompany.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Servicio para gestión de Ventas
 * Incluye lógica completa de proceso de venta con control de inventario
 */
@Service
@Transactional
public class VentaService {
    private static final Logger logger = LoggerFactory.getLogger(VentaService.class);

    @Autowired
    private VentaRepository ventaRepository;


    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private PromocionRepository promocionRepository;

    @Autowired
    private com.mycompany.service.InventoryService inventoryService;

    public List<Venta> listarTodas() {
        return ventaRepository.findAll();
    }

    public List<Venta> listarPorUsuario(Integer idUsuario) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
            .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        return ventaRepository.findByUsuario(usuario);
    }

    public List<Venta> listarPorCliente(Integer idCliente) {
        Cliente cliente = clienteRepository.findById(idCliente)
            .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado"));
        return ventaRepository.findByCliente(cliente);
    }

    public List<Venta> listarPorRangoFechas(LocalDateTime inicio, LocalDateTime fin) {
        return ventaRepository.findByRangoFechas(inicio, fin);
    }

    public Optional<Venta> buscarPorId(Integer id) {
        return ventaRepository.findById(id);
    }

    /**
     * Crear nueva venta con validación de stock y actualización de inventario
     */
    public Venta crearVenta(VentaDTO ventaDTO) {
        try {
            logger.info("crearVenta recibido - idUsuario={}, detalles.size={}, total={}",
                ventaDTO.getIdUsuario(),
                ventaDTO.getDetalles() != null ? ventaDTO.getDetalles().size() : 0,
                ventaDTO.getTotal());
            // Validar usuario vendedor
            Usuario usuario = usuarioRepository.findById(ventaDTO.getIdUsuario())
                .orElseThrow(() -> new IllegalArgumentException("Usuario vendedor no encontrado"));

        // Obtener cliente (opcional)
        Cliente cliente = null;
        if (ventaDTO.getIdCliente() != null) {
            cliente = clienteRepository.findById(ventaDTO.getIdCliente())
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado"));
        }

        // Crear venta
        Venta venta = new Venta();
        venta.setUsuario(usuario);
        venta.setCliente(cliente);
        venta.setMetodoPago(ventaDTO.getMetodoPago());
        venta.setDescuento(ventaDTO.getDescuento() != null ? ventaDTO.getDescuento() : BigDecimal.ZERO);
        venta.setComprobante(ventaDTO.getComprobante());
        venta.setObservaciones(ventaDTO.getObservaciones());

        // Procesar detalles
        for (DetalleVentaDTO detalleDTO : ventaDTO.getDetalles()) {
            Producto producto = productoRepository.findById(detalleDTO.getIdProducto())
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado: " + detalleDTO.getIdProducto()));

            // Validar stock disponible
            if (!producto.hayStock(detalleDTO.getCantidad())) {
                throw new IllegalStateException("Stock insuficiente para: " + producto.getNombre() + 
                    ". Disponible: " + producto.getStock());
            }

            // Verificar si hay promoción vigente
            BigDecimal precioFinal = producto.getPrecio();
            Optional<Promocion> promocion = promocionRepository.findPromocionVigenteParaProducto(producto.getIdProducto());
            if (promocion.isPresent()) {
                precioFinal = promocion.get().calcularPrecioConDescuento(precioFinal);
            }

            // Crear detalle
            DetalleVenta detalle = new DetalleVenta();
            detalle.setProducto(producto);
            detalle.setCantidad(detalleDTO.getCantidad());
            detalle.setPrecioUnitario(precioFinal);
            detalle.setDescuento(detalleDTO.getDescuento() != null ? detalleDTO.getDescuento() : BigDecimal.ZERO);
            
            venta.agregarDetalle(detalle);

            // Reducir stock
            producto.reducirStock(detalleDTO.getCantidad());
            productoRepository.save(producto);
        }

            logger.info("Preparando guardar venta - usuario={}, detalles={}, total={}", usuario.getNombre(), venta.getDetalles().size(), ventaDTO.getTotal());
            Venta saved = ventaRepository.save(venta);
            logger.info("Venta persistida - idVenta={}, detallesSaved={}", saved.getIdVenta(), saved.getDetalles() != null ? saved.getDetalles().size() : 0);
            // Registrar movimientos de stock a través de InventoryService
            inventoryService.registerForSale(saved.getDetalles(), saved.getIdVenta());
            return saved;
        } catch (Exception ex) {
            logger.error("Excepción al crear venta: mensaje='{}'", ex.getMessage(), ex);
            throw ex;
        }
    }

    /**
     * Anular venta y devolver stock
     */
    public void anularVenta(Integer idVenta) {
        Venta venta = ventaRepository.findById(idVenta)
            .orElseThrow(() -> new IllegalArgumentException("Venta no encontrada"));

        if (venta.isAnulada()) {
            throw new IllegalStateException("La venta ya está anulada");
        }

        // Devolver stock de productos
        for (DetalleVenta detalle : venta.getDetalles()) {
            Producto producto = detalle.getProducto();
            producto.aumentarStock(detalle.getCantidad());
            productoRepository.save(producto);
        }

        // Anular venta
        venta.anular();
        ventaRepository.save(venta);
        logger.info("Venta anulada: {}", idVenta);
    }

    /**
     * Calcular total de ventas en rango de fechas
     */
    public BigDecimal calcularTotalVentas(LocalDateTime inicio, LocalDateTime fin) {
        BigDecimal total = ventaRepository.calcularTotalVentasEnRango(inicio, fin);
        return total != null ? total : BigDecimal.ZERO;
    }

    /**
     * Calcular ventas del día actual
     */
    public BigDecimal calcularVentasDelDia() {
        BigDecimal total = ventaRepository.calcularVentasDelDia();
        return total != null ? total : BigDecimal.ZERO;
    }

    /**
     * Contar ventas en rango de fechas
     */
    public long contarVentas(LocalDateTime inicio, LocalDateTime fin) {
        return ventaRepository.contarVentasEnRango(inicio, fin);
    }

    /**
     * Obtener últimas ventas
     */
    public List<Venta> obtenerUltimasVentas() {
        return ventaRepository.findUltimasVentas();
    }

    // Conversión DTO
    public VentaDTO convertirADTO(Venta venta) {
        VentaDTO dto = new VentaDTO();
        dto.setIdVenta(venta.getIdVenta());
        dto.setIdUsuario(venta.getUsuario().getIdUsuario());
        dto.setNombreUsuario(venta.getUsuario().getNombre());
        
        if (venta.getCliente() != null) {
            dto.setIdCliente(venta.getCliente().getIdCliente());
            dto.setNombreCliente(venta.getCliente().getNombreCompleto());
        }
        
        dto.setFecha(venta.getFecha());
        dto.setMetodoPago(venta.getMetodoPago());
        dto.setTotal(venta.getTotal());
        dto.setDescuento(venta.getDescuento());
        dto.setComprobante(venta.getComprobante());
        dto.setEstado(venta.getEstado());
        dto.setObservaciones(venta.getObservaciones());

        // Convertir detalles
        List<DetalleVentaDTO> detallesDTO = venta.getDetalles().stream()
            .map(this::convertirDetalleADTO)
            .collect(Collectors.toList());
        dto.setDetalles(detallesDTO);

        return dto;
    }

    public DetalleVentaDTO convertirDetalleADTO(DetalleVenta detalle) {
        DetalleVentaDTO dto = new DetalleVentaDTO();
        dto.setIdDetalle(detalle.getIdDetalle());
        dto.setIdProducto(detalle.getProducto().getIdProducto());
        dto.setNombreProducto(detalle.getProducto().getNombre());
        dto.setCantidad(detalle.getCantidad());
        dto.setPrecioUnitario(detalle.getPrecioUnitario());
        dto.setDescuento(detalle.getDescuento());
        dto.setSubtotal(detalle.getSubtotal());
        return dto;
    }

    public List<VentaDTO> convertirListaADTO(List<Venta> ventas) {
        return ventas.stream()
            .map(this::convertirADTO)
            .collect(Collectors.toList());
    }

    // Métodos legacy para compatibilidad
    @Deprecated
    public void registrarVenta(Venta venta) {
        ventaRepository.save(venta);
    }

    @Deprecated
    public List<Venta> obtenerVentas() {
        return listarTodas();
    }

    @Deprecated
    public Venta buscarVentaPorId(int id) {
        return buscarPorId(id).orElse(null);
    }
}