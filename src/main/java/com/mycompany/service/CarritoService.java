package com.mycompany.service;

import com.mycompany.model.Carrito;
import com.mycompany.model.CarritoDetalle;
import com.mycompany.model.Producto;
import com.mycompany.model.Venta;
import com.mycompany.dto.VentaDTO;
import com.mycompany.dto.DetalleVentaDTO;
import com.mycompany.repository.CarritoRepository;
import com.mycompany.repository.CarritoDetalleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CarritoService {

    @Autowired
    private CarritoRepository carritoRepository;

    @Autowired
    private CarritoDetalleRepository carritoDetalleRepository;

    @Autowired
    private ProductoService productoService;

    @Autowired
    private VentaService ventaService;

    public Carrito agregarProducto(Integer idCarrito, Integer idProducto, Integer cantidad) {
        Carrito carrito = carritoRepository.findById(idCarrito)
            .orElseThrow(() -> new IllegalArgumentException("Carrito no encontrado"));

        Producto producto = productoService.buscarPorId(idProducto)
            .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));

        CarritoDetalle detalle = new CarritoDetalle();
        detalle.setCarrito(carrito);
        detalle.setProducto(producto);
        detalle.setCantidad(cantidad);
        detalle.setPrecioUnitario(producto.getPrecio());
        detalle.setDescuento(BigDecimal.ZERO);
        detalle.setSubtotal(producto.getPrecio().multiply(new BigDecimal(cantidad)));

        carritoDetalleRepository.save(detalle);
        return carrito;
    }

    public void vaciarCarrito(Integer idCarrito) {
        carritoDetalleRepository.deleteByCarrito_IdCarrito(idCarrito);
    }

    public Venta convertirAVenta(Integer idCarrito) {
        Carrito carrito = carritoRepository.findById(idCarrito)
            .orElseThrow(() -> new IllegalArgumentException("Carrito no encontrado"));

        List<CarritoDetalle> detalles = carritoDetalleRepository.findByCarrito_IdCarrito(idCarrito);
        if (detalles.isEmpty()) {
            throw new IllegalStateException("El carrito está vacío");
        }

        VentaDTO ventaDTO = new VentaDTO();
        ventaDTO.setIdUsuario(carrito.getUsuario().getIdUsuario());
        ventaDTO.setMetodoPago("EFECTIVO");
        ventaDTO.setComprobante("AUTO");

        List<DetalleVentaDTO> detallesDTO = detalles.stream().map(d -> {
            DetalleVentaDTO dd = new DetalleVentaDTO();
            dd.setIdProducto(d.getProducto().getIdProducto());
            dd.setCantidad(d.getCantidad());
            dd.setPrecioUnitario(d.getPrecioUnitario());
            dd.setDescuento(d.getDescuento());
            return dd;
        }).collect(Collectors.toList());

        ventaDTO.setDetalles(detallesDTO);

        Venta venta = ventaService.crearVenta(ventaDTO);

        // borrar detalles y actualizar carrito
        carritoDetalleRepository.deleteByCarrito_IdCarrito(idCarrito);
        carrito.setEstado("VENDIDO");
        carritoRepository.save(carrito);

        return venta;
    }

    public List<CarritoDetalle> obtenerDetallesPorCarrito(Integer idCarrito) {
        return carritoDetalleRepository.findByCarrito_IdCarrito(idCarrito);
    }
}
