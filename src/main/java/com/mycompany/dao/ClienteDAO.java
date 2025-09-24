/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.dao;
import com.mycompany.model.Cliente;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author LUIGGI
 */
public class ClienteDAO {

    private final JdbcTemplate jdbcTemplate;

    public ClienteDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Guardar un nuevo cliente
    public void guardarCliente(Cliente cliente) {
        String sql = "INSERT INTO clientes (nombre, email, telefono) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, cliente.getNombre(), cliente.getEmail(), cliente.getTelefono());
    }

    // Obtener todos los clientes
    public List<Cliente> obtenerClientes() {
        String sql = "SELECT * FROM clientes";
        return jdbcTemplate.query(sql, new ClienteRowMapper());
    }

    // Buscar cliente por ID
    public Cliente buscarPorId(int id) {
        String sql = "SELECT * FROM clientes WHERE id = ?";
        List<Cliente> clientes = jdbcTemplate.query(sql, new Object[]{id}, new ClienteRowMapper());
        return clientes.isEmpty() ? null : clientes.get(0);
    }

    // RowMapper para mapear resultados
    private static class ClienteRowMapper implements RowMapper<Cliente> {
        @Override
        public Cliente mapRow(ResultSet rs, int rowNum) throws SQLException {
            Cliente cliente = new Cliente();
            cliente.setId(rs.getInt("id"));
            cliente.setNombre(rs.getString("nombre"));
            cliente.setEmail(rs.getString("email"));
            cliente.setTelefono(rs.getString("telefono"));
            return cliente;
        }
    }
}