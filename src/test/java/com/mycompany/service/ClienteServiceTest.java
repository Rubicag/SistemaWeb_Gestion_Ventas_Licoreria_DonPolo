
package com.mycompany.service;

import com.mycompany.model.Cliente;
import com.mycompany.repository.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.util.List;
import java.util.ArrayList;

class ClienteServiceTest {
    @Test
    void testRegistrarCliente() {
        Cliente cliente = new Cliente();
        when(clienteRepository.save(cliente)).thenReturn(cliente);
        clienteService.registrarCliente(cliente);
        verify(clienteRepository).save(cliente);
    }

    @Test
    void testObtenerClientes() {
        List<Cliente> clientes = new ArrayList<>();
        when(clienteRepository.findAll()).thenReturn(clientes);
        List<Cliente> resultado = clienteService.obtenerClientes();
        verify(clienteRepository).findAll();
        assertSame(clientes, resultado);
    }

    @Test
    void testBuscarClientePorId() {
        int id = 1;
        Cliente cliente = new Cliente();
        when(clienteRepository.findById(id)).thenReturn(java.util.Optional.of(cliente));
        Cliente resultado = clienteService.buscarClientePorId(id);
        verify(clienteRepository).findById(id);
        assertSame(cliente, resultado);
    }
    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteService clienteService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testActualizarCliente() {
        Cliente cliente = new Cliente();
    cliente.setId(1);
    when(clienteRepository.save(cliente)).thenReturn(cliente);
    clienteService.actualizarCliente(cliente);
    verify(clienteRepository).save(cliente);
    }

    @Test
    void testEliminarCliente() {
        int id = 1;
        doNothing().when(clienteRepository).deleteById(id);
        clienteService.eliminarCliente(id);
        verify(clienteRepository).deleteById(id);
    }
}
