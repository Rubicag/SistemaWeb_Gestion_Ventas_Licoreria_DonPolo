package com.mycompany.service;

import com.mycompany.model.Carrito;
import com.mycompany.model.Producto;
import com.mycompany.model.Usuario;
import com.mycompany.repository.CarritoRepository;
import com.mycompany.repository.ProductoRepository;
import com.mycompany.repository.UsuarioRepository;
import com.mycompany.repository.CarritoDetalleRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@SpringBootTest
@Transactional
public class CarritoServiceTest {

    @Autowired
    private CarritoService carritoService;

    @Autowired
    private CarritoRepository carritoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private CarritoDetalleRepository carritoDetalleRepository;

    @Test
    public void agregarProducto_creaDetalle() {
        Usuario u = new Usuario();
        u.setNombre("TestUser");
        usuarioRepository.save(u);

        Carrito c = new Carrito();
        c.setUsuario(u);
        c.setFechaCreacion(new java.util.Date());
        c.setEstado("ACTIVO");
        carritoRepository.save(c);

        Producto p = new Producto();
        p.setNombre("Producto Test");
        p.setPrecio(new BigDecimal("10.00"));
        p.setStock(100);
        p.setCategoria(null);
        p.setProveedor(null);
        productoRepository.save(p);

        carritoService.agregarProducto(c.getIdCarrito(), p.getIdProducto(), 2);

        var detalles = carritoDetalleRepository.findByCarrito_IdCarrito(c.getIdCarrito());
        Assertions.assertFalse(detalles.isEmpty(), "Detalles no deberían estar vacíos");
        Assertions.assertEquals(1, detalles.size());
        Assertions.assertEquals(2, detalles.get(0).getCantidad());
    }
}
