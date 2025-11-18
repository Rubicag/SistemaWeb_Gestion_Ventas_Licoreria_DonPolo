package com.mycompany.service;

import com.mycompany.model.Producto;
import com.mycompany.model.Usuario;
import com.mycompany.dto.VentaDTO;
import com.mycompany.dto.DetalleVentaDTO;
import com.mycompany.repository.ProductoRepository;
import com.mycompany.repository.UsuarioRepository;
import com.mycompany.repository.MovimientoStockRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@SpringBootTest
@Transactional
public class VentaServiceInventoryTest {

    @Autowired
    private VentaService ventaService;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private MovimientoStockRepository movimientoStockRepository;

    @Test
    public void crearVenta_registraMovimientoStock() {
        Usuario u = new Usuario();
        u.setNombre("Vendedor");
        usuarioRepository.save(u);

        Producto p = new Producto();
        p.setNombre("P Venta");
        p.setPrecio(new BigDecimal("20.00"));
        p.setStock(10);
        p.setCategoria(null);
        p.setProveedor(null);
        productoRepository.save(p);

        VentaDTO venta = new VentaDTO();
        venta.setIdUsuario(u.getIdUsuario());
        DetalleVentaDTO d = new DetalleVentaDTO();
        d.setIdProducto(p.getIdProducto());
        d.setCantidad(2);
        d.setPrecioUnitario(p.getPrecio());
        venta.setDetalles(List.of(d));

        ventaService.crearVenta(venta);

        var movimientos = movimientoStockRepository.findAll();
        Assertions.assertFalse(movimientos.isEmpty());
        Assertions.assertEquals("OUT", movimientos.get(0).getTipo());
    }
}
