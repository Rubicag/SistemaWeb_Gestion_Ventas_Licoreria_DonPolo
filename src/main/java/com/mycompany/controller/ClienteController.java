package com.mycompany.controller;

import com.mycompany.dto.ClienteDTO;
import com.mycompany.model.Cliente;
import com.mycompany.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * Controlador para gestión de Clientes
 */
@Controller
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping({"/listar", "/", ""})
    public String listarClientes(Model model) {
        List<ClienteDTO> clientes = clienteService.convertirListaADTO(clienteService.listarActivos());
        model.addAttribute("clientes", clientes);
        return "clientes";
    }

    @GetMapping("/nuevo")
    public String nuevoCliente(Model model) {
        model.addAttribute("cliente", new ClienteDTO());
        List<ClienteDTO> clientes = clienteService.convertirListaADTO(clienteService.listarActivos());
        model.addAttribute("clientes", clientes);
        return "clientes";
    }

    @PostMapping("/guardar")
    public String guardarCliente(@Valid @ModelAttribute("cliente") ClienteDTO clienteDTO, 
                                BindingResult result, 
                                RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", "Error en los datos ingresados");
            return "redirect:/clientes";
        }

        try {
            Cliente cliente = clienteService.convertirAEntidad(clienteDTO);
            clienteService.guardar(cliente);
            redirectAttributes.addFlashAttribute("mensaje", "Cliente guardado exitosamente");
            return "redirect:/clientes";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/clientes";
        }
    }

    @GetMapping("/editar/{id}")
    public String editarCliente(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        Cliente cliente = clienteService.buscarPorId(id)
            .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado"));
        model.addAttribute("cliente", clienteService.convertirADTO(cliente));
        List<ClienteDTO> clientes = clienteService.convertirListaADTO(clienteService.listarActivos());
        model.addAttribute("clientes", clientes);
        return "clientes";
    }

    @PostMapping("/actualizar")
    public String actualizarCliente(@Valid @ModelAttribute("cliente") ClienteDTO clienteDTO,
                                    BindingResult result,
                                    RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", "Error en los datos ingresados");
            return "redirect:/clientes";
        }

        try {
            Cliente cliente = clienteService.convertirAEntidad(clienteDTO);
            clienteService.actualizar(cliente);
            redirectAttributes.addFlashAttribute("mensaje", "Cliente actualizado exitosamente");
            return "redirect:/clientes";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/clientes";
        }
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarCliente(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            clienteService.eliminar(id);
            redirectAttributes.addFlashAttribute("mensaje", "Cliente eliminado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar cliente: " + e.getMessage());
        }
        return "redirect:/clientes";
    }

    @GetMapping("/buscar")
    public String buscarClientes(@RequestParam("termino") String termino, Model model) {
        List<Cliente> clientes = clienteService.buscar(termino);
        model.addAttribute("clientes", clienteService.convertirListaADTO(clientes));
        model.addAttribute("termino", termino);
        return "clientes";
    }
}

