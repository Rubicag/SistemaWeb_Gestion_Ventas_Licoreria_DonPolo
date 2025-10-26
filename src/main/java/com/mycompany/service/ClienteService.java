/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.service;

import org.springframework.stereotype.Service;
import com.mycompany.repository.ClienteRepository;
import com.mycompany.model.Cliente;
import java.util.List;
/**
 *
 * @author LUIGGI
 */
@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    // Registrar un cliente
    public void registrarCliente(Cliente cliente) {
        clienteRepository.save(cliente);
    }

    // Obtener todos los clientes
    public List<Cliente> obtenerClientes() {
        return clienteRepository.findAll();
    }


    // Buscar cliente por ID
    public Cliente buscarClientePorId(int id) {
        return clienteRepository.findById(id).orElse(null);
    }

    // Actualizar cliente
    public void actualizarCliente(Cliente cliente) {
        clienteRepository.save(cliente);
    }

    // Eliminar cliente
    public void eliminarCliente(int id) {
        clienteRepository.deleteById(id);
    }

    // Buscar cliente por email
    public Cliente buscarClientePorCorreo(String email) {
        return clienteRepository.findByEmail(email).orElse(null);
    }
}