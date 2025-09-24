/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.dao;
import com.mycompany.model.Venta;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
/**
 *
 * @author LUIGGI
 */
public class VentaDAO {

    private final JdbcTemplate jdbcTemplate;

    public VentaDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Guardar una venta
    public void guardarVenta(Venta venta) {
        String sql = "INSERT INTO ventas (idCliente, idProducto, cantidad, total, fecha) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                venta.getIdCliente(),
                venta.getIdProducto(),
                venta.getCantidad(),
                venta.getTotal(),
                venta.getFecha());
    }

    // Obtener todas las ventas
    public List<Venta> obtenerVentas() {
        String sql = "SELECT * FROM ventas";
        return jdbcTemplate.query(sql, new VentaRowMapper());
    }
    public Venta buscarPorId(int id) {
    String sql = "SELECT * FROM ventas WHERE id = ?";
    List<Venta> ventas = jdbcTemplate.query(sql, new Object[]{id}, new VentaRowMapper());
    return ventas.isEmpty() ? null : ventas.get(0);
}

    // RowMapper para mapear resultados
    private static class VentaRowMapper implements RowMapper<Venta> {
        @Override
        public Venta mapRow(ResultSet rs, int rowNum) throws SQLException {
            Venta venta = new Venta();
            venta.setId(rs.getInt("id"));
            venta.setIdCliente(rs.getInt("idCliente"));
            venta.setIdProducto(rs.getInt("idProducto"));
            venta.setCantidad(rs.getInt("cantidad"));
            venta.setTotal(rs.getDouble("total"));
            venta.setFecha(rs.getDate("fecha"));
            return venta;
        }
        
    }
}
