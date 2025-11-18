package com.mycompany.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.mycompany.service.PedidoService;
import com.mycompany.service.UsuarioService;
import com.mycompany.dto.PedidoSummaryDTO;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/pedidos")
public class PedidoController {
    private final PedidoService pedidoService;
    
    @Autowired
    private UsuarioService usuarioService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @GetMapping({"", "/", "/listar"})
    public String listarPedidos(Model model) {
        var pedidos = pedidoService.obtenerPedidos();
        model.addAttribute("pedidos", pedidos);
        model.addAttribute("usuarios", usuarioService.obtenerUsuarios());

        // Mapear a DTOs ligeros para inyectar en JS sin referencias cíclicas
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        List<PedidoSummaryDTO> pedidosDto = pedidos.stream().map(p -> {
            String fechaIso = p.getFecha() != null ? p.getFecha().format(fmt) : null;
            String usuarioNombre = p.getUsuario() != null ? p.getUsuario().getNombre() : null;
            return new PedidoSummaryDTO(p.getIdPedido(), fechaIso, p.getDireccionEntrega(), p.getEstado(), usuarioNombre);
        }).collect(Collectors.toList());
        model.addAttribute("pedidosDto", pedidosDto);
        return "pedidos";
    }

    @GetMapping("/cambiarEstado/{id}/{nuevoEstado}")
    public String cambiarEstado(@PathVariable Integer id, @PathVariable String nuevoEstado,
                                RedirectAttributes redirectAttributes) {
        try {
            pedidoService.cambiarEstado(id, nuevoEstado);
            redirectAttributes.addFlashAttribute("mensaje", "Estado cambiado a " + nuevoEstado);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/pedidos";
    }

    // Otros métodos (guardar, actualizar) pueden añadirse más adelante
}
