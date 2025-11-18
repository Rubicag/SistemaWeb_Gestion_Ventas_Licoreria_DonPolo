package com.mycompany.controller;

import com.mycompany.dto.ProveedorSimpleDTO;
import com.mycompany.model.Proveedor;
import com.mycompany.service.ProveedorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/proveedores")
public class ProveedorController {
    @Autowired
    private ProveedorService proveedorService;

    @GetMapping({"", "/", "/listar"})
    public String listarProveedores(Model model) {
        // Convertir proveedores a DTO simple para evitar referencias circulares
        List<ProveedorSimpleDTO> proveedoresDTO = proveedorService.listarProveedores().stream()
            .map(p -> new ProveedorSimpleDTO(
                p.getIdProveedor(),
                p.getNombre(),
                p.getRuc(),
                p.getContacto(),
                p.getTelefono(),
                p.getEmail(),
                p.getDireccion(),
                p.isActivo()
            ))
            .collect(Collectors.toList());
        model.addAttribute("proveedores", proveedoresDTO);
        return "proveedores";
    }

    @PostMapping("/guardar")
    public String guardarProveedor(@Valid @ModelAttribute("proveedor") Proveedor proveedor,
                                   BindingResult result,
                                   Model model,
                                   RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            List<ProveedorSimpleDTO> proveedoresDTO = proveedorService.listarProveedores().stream()
                .map(p -> new ProveedorSimpleDTO(
                    p.getIdProveedor(), p.getNombre(), p.getRuc(), p.getContacto(), p.getTelefono(), p.getEmail(), p.getDireccion(), p.isActivo()
                ))
                .collect(Collectors.toList());
            model.addAttribute("proveedores", proveedoresDTO);
            return "proveedores";
        }

        try {
            proveedorService.guardarProveedor(proveedor);
            redirectAttributes.addFlashAttribute("mensaje", "Proveedor guardado exitosamente");
            return "redirect:/proveedores";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/proveedores";
        }
    }

    @PostMapping("/actualizar")
    public String actualizarProveedor(@Valid @ModelAttribute("proveedor") Proveedor proveedor,
                                      BindingResult result,
                                      Model model,
                                      RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            List<ProveedorSimpleDTO> proveedoresDTO = proveedorService.listarProveedores().stream()
                .map(p -> new ProveedorSimpleDTO(
                    p.getIdProveedor(), p.getNombre(), p.getRuc(), p.getContacto(), p.getTelefono(), p.getEmail(), p.getDireccion(), p.isActivo()
                ))
                .collect(Collectors.toList());
            model.addAttribute("proveedores", proveedoresDTO);
            return "proveedores";
        }

        try {
            proveedorService.actualizar(proveedor);
            redirectAttributes.addFlashAttribute("mensaje", "Proveedor actualizado exitosamente");
            return "redirect:/proveedores";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/proveedores";
        }
    }

    @GetMapping("/cambiarEstado/{id}")
    public String cambiarEstado(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            Proveedor proveedor = proveedorService.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Proveedor no encontrado"));
            boolean nuevoEstado = !proveedor.isActivo();
            if (nuevoEstado) {
                proveedorService.activar(id);
            } else {
                // Desactivar (usar eliminar que marca como inactivo)
                proveedorService.eliminar(id);
            }
            String estado = nuevoEstado ? "activado" : "desactivado";
            redirectAttributes.addFlashAttribute("mensaje", "Proveedor " + estado + " exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al cambiar estado: " + e.getMessage());
        }
        return "redirect:/proveedores";
    }
}


