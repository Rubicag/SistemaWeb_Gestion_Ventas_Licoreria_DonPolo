package com.mycompany.controller;

import com.mycompany.model.Cliente;
import com.mycompany.service.ClienteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClienteControllerTest {
    @Mock
    private ClienteService clienteService;

    @Mock
    private Model model;

    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private ClienteController clienteController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testListarClientes() {
        when(clienteService.obtenerClientes()).thenReturn(Arrays.asList(new Cliente()));
        String view = clienteController.listarClientes(model);
        assertEquals("clientes/listar", view);
        verify(model).addAttribute(eq("clientes"), anyList());
    }

    @Test
    void testMostrarFormularioEditar() {
        Cliente cliente = new Cliente();
        when(clienteService.buscarClientePorId(1)).thenReturn(cliente);
        String view = clienteController.mostrarFormularioEditar(1, model);
        assertEquals("clientes/editar", view);
        verify(model).addAttribute("cliente", cliente);
    }

    @Test
    void testEliminarCliente() {
        String view = clienteController.eliminarCliente(1);
    assertEquals("redirect:/clientes/listar", view);
        verify(clienteService).eliminarCliente(1);
    }
}
