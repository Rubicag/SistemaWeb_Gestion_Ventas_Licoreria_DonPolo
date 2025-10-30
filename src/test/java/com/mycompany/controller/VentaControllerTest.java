package com.mycompany.controller;

import com.mycompany.model.Venta;
import com.mycompany.service.UsuarioService;
import com.mycompany.service.VentaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VentaControllerTest {
    @Mock
    private VentaService ventaService;
    @Mock
    private UsuarioService usuarioService;
    @Mock
    private Model model;
    @InjectMocks
    private VentaController ventaController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testHistorialCompras() {
        when(ventaService.obtenerVentas()).thenReturn(Arrays.asList(new Venta()));
        String view = ventaController.historialCompras(model);
        assertEquals("ventas/listar", view);
        verify(model).addAttribute(eq("ventas"), anyList());
    }
    @Test
    void testMostrarFormularioEditar() {
        Venta venta = new Venta();
        when(ventaService.buscarVentaPorId(1)).thenReturn(venta);
        when(usuarioService.obtenerUsuarios()).thenReturn(Arrays.asList());
        String view = ventaController.mostrarFormularioEditar(1, model);
        assertEquals("ventas/editar", view);
        verify(model).addAttribute("venta", venta);
        verify(model).addAttribute(eq("usuarios"), anyList());
    }
}
