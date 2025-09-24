/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.dao;
import com.mycompany.model.Producto;
import org.springframework.jdbc.core.JdbcTemplate;
import java.util.List;
/**
 *
 * @author LUIGGI
 */
public class ProductoDAO {
  private final JdbcTemplate jdbcTemplate;

    // Constructor que recibe el JdbcTemplate configurado
    public ProductoDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Método para obtener todos los productos
    public List<Producto> obtenerTodosLosProductos() {
        String sql = "SELECT * FROM productos";
        return jdbcTemplate.query(sql, new ProductoRowMapper());
    }

    // Método para agregar un producto
    public void agregarProducto(Producto producto) {
        String sql = "INSERT INTO productos (nombre, precio) VALUES (?, ?)";
        jdbcTemplate.update(sql, producto.getNombre(), producto.getPrecio());
    }

    // Método para eliminar un producto por ID
    public void eliminarProducto(int productoId) {
        String sql = "DELETE FROM productos WHERE id = ?";
        jdbcTemplate.update(sql, productoId);
    }

 public Producto buscarPorId(int id) {
    String sql = "SELECT * FROM productos WHERE id = ?";
    List<Producto> productos = jdbcTemplate.query(sql, new Object[]{id}, new ProductoRowMapper());
    return productos.isEmpty() ? null : productos.get(0);
}

}