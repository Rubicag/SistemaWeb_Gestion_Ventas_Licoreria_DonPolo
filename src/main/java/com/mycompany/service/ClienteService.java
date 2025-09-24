/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.service;
import com.mycompany.dao.ClienteDAO;
import com.mycompany.model.Cliente;
import java.util.List;
/**
 *
 * @author LUIGGI
 */
public class ClienteService {

    private final ClienteDAO clienteDAO;

    public ClienteService(ClienteDAO clienteDAO) {
        this.clienteDAO = clienteDAO;
    }

    // Registrar un cliente
    public void registrarCliente(Cliente cliente) {
        clienteDAO.guardarCliente(cliente);
    }

    // Obtener todos los clientes
    public List<Cliente> obtenerClientes() {
        return clienteDAO.obtenerClientes();
    }

    // Buscar cliente por ID
    public Cliente buscarClientePorId(int id) {
        return clienteDAO.buscarPorId(id);
    }
}