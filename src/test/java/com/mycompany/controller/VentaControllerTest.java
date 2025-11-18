package com.mycompany.controller;

import com.mycompany.dto.VentaDTO;
import com.mycompany.model.Venta;
import com.mycompany.service.VentaService;
import com.mycompany.service.ClienteService;
import com.mycompany.service.ProductoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VentaControllerTest {
    @Mock
    private VentaService ventaService;
    @Mock
    private ClienteService clienteService;
    @Mock
    private ProductoService productoService;
    @Mock
    private Model model;
    @Mock
    private RedirectAttributes redirectAttributes;
    @InjectMocks
    private VentaController ventaController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testListarVentas() {
        when(ventaService.listarTodas()).thenReturn(Arrays.asList(new Venta()));
        when(ventaService.convertirListaADTO(anyList())).thenReturn(Arrays.asList(new VentaDTO()));
        when(clienteService.listarActivos()).thenReturn(Arrays.asList());
        when(productoService.listarDisponibles()).thenReturn(Arrays.asList());
        when(ventaService.calcularVentasDelDia()).thenReturn(BigDecimal.ZERO);
        
        String view = ventaController.listarVentas(model);
        
        assertEquals("ventas", view);
        verify(model).addAttribute(eq("ventas"), anyList());
        verify(model).addAttribute(eq("clientes"), anyList());
        verify(model).addAttribute(eq("productos"), anyList());
        verify(model).addAttribute(eq("ventasDelDia"), any(BigDecimal.class));
    }
    
    @Test
    void testDetalleVenta() {
        Venta venta = new Venta();
        venta.setIdVenta(1);
        VentaDTO ventaDTO = new VentaDTO();
        
        when(ventaService.buscarPorId(1)).thenReturn(Optional.of(venta));
        when(ventaService.convertirADTO(venta)).thenReturn(ventaDTO);
        when(ventaService.listarTodas()).thenReturn(Arrays.asList(venta));
        when(ventaService.convertirListaADTO(anyList())).thenReturn(Arrays.asList(ventaDTO));
        when(clienteService.listarActivos()).thenReturn(Arrays.asList());
        when(productoService.listarDisponibles()).thenReturn(Arrays.asList());
        
        String view = ventaController.detalleVenta(1, model, redirectAttributes);
        
        assertEquals("ventas", view);
        verify(model).addAttribute(eq("venta"), any(VentaDTO.class));
        verify(model).addAttribute(eq("ventas"), anyList());
    }
    
    @Test
    void testAnularVenta() {
        doNothing().when(ventaService).anularVenta(1);
        
        String view = ventaController.anularVenta(1, redirectAttributes);
        
        assertEquals("redirect:/ventas", view);
        verify(redirectAttributes).addFlashAttribute(eq("mensaje"), anyString());
    }
}
