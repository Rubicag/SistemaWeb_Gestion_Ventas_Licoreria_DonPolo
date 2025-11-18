package com.mycompany.repository;

import com.mycompany.model.Carrito;
import com.mycompany.model.CarritoDetalle;
import com.mycompany.model.Producto;
import com.mycompany.model.Usuario;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;

@DataJpaTest
public class CarritoDetalleRepositoryTest {

    @Autowired
    private CarritoDetalleRepository carritoDetalleRepository;

    @Autowired
    private CarritoRepository carritoRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    public void saveAndFindByCarrito() {
        Usuario u = new Usuario();
        u.setNombre("RepoUser");
        usuarioRepository.save(u);

        Carrito c = new Carrito();
        c.setUsuario(u);
        c.setFechaCreacion(new java.util.Date());
        c.setEstado("ACTIVO");
        carritoRepository.save(c);

        Producto p = new Producto();
        p.setNombre("P Repo");
        p.setPrecio(new BigDecimal("5.00"));
        p.setStock(50);
        p.setCategoria(null);
        p.setProveedor(null);
        productoRepository.save(p);

        CarritoDetalle d = new CarritoDetalle();
        d.setCarrito(c);
        d.setProducto(p);
        d.setCantidad(3);
        carritoDetalleRepository.save(d);

        var encontrados = carritoDetalleRepository.findByCarrito_IdCarrito(c.getIdCarrito());
        Assertions.assertEquals(1, encontrados.size());
        Assertions.assertEquals(3, encontrados.get(0).getCantidad());
    }
}
