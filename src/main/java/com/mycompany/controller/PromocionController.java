package com.mycompany.controller;

import com.mycompany.model.Promocion;
import com.mycompany.dto.PromocionSimpleDTO;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import com.mycompany.dto.ProductoSimpleDTO;
import com.mycompany.service.PromocionService;
import com.mycompany.service.ProductoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/promociones")
public class PromocionController {
    private final PromocionService promocionService;
    
    @Autowired
    private ProductoService productoService;

    public PromocionController(PromocionService promocionService) {
        this.promocionService = promocionService;
    }

    @GetMapping({"", "/", "/listar"})
    public String listarPromociones(Model model) {
        // Convertir entidades a DTOs para evitar propiedades faltantes en las vistas
        var promos = promocionService.obtenerPromociones();
        var dtoList = promos.stream().map(p -> {
            String tipo = "PORCENTAJE";
            if (p.getDescuento() == null) tipo = "";
            // Estado derivado
            String estado;
            LocalDate hoy = LocalDate.now();
            if (!p.isActivo()) {
                estado = "FINALIZADA";
            } else if (hoy.isBefore(p.getFechaInicio())) {
                estado = "PROGRAMADA";
            } else if (hoy.isAfter(p.getFechaFin())) {
                estado = "FINALIZADA";
            } else {
                estado = "ACTIVA";
            }
            return new PromocionSimpleDTO(p.getIdPromocion(), p.getNombre(), p.getDescripcion(),
                    p.getDescuento(), p.getFechaInicio(), p.getFechaFin(), tipo, estado);
        }).collect(Collectors.toList());

        model.addAttribute("promociones", dtoList);
        List<ProductoSimpleDTO> productosDTO = productoService.listarDisponibles().stream()
            .map(p -> new ProductoSimpleDTO(
                p.getIdProducto(), p.getNombre(), p.getDescripcion(),
                p.getPrecio(), p.getStock(),
                p.getCategoria() != null ? p.getCategoria().getNombre() : "Sin categoría",
                p.getProveedor() != null ? p.getProveedor().getNombre() : "Sin proveedor",
                p.isActivo()
            ))
            .collect(Collectors.toList());
        model.addAttribute("productos", productosDTO);
        return "promociones";
    }

    @PostMapping("/guardar")
    public String guardarPromocion(@Valid @ModelAttribute("promocion") Promocion promocion,
                                   BindingResult result,
                                   Model model,
                                   RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            var promos = promocionService.obtenerPromociones();
            var dtoList = promos.stream().map(p -> {
                String tipo = "PORCENTAJE";
                if (p.getDescuento() == null) tipo = "";
                String estado;
                LocalDate hoy = LocalDate.now();
                if (!p.isActivo()) {
                    estado = "FINALIZADA";
                } else if (hoy.isBefore(p.getFechaInicio())) {
                    estado = "PROGRAMADA";
                } else if (hoy.isAfter(p.getFechaFin())) {
                    estado = "FINALIZADA";
                } else {
                    estado = "ACTIVA";
                }
                return new PromocionSimpleDTO(p.getIdPromocion(), p.getNombre(), p.getDescripcion(),
                        p.getDescuento(), p.getFechaInicio(), p.getFechaFin(), tipo, estado);
            }).collect(Collectors.toList());
                model.addAttribute("promociones", dtoList);
                List<ProductoSimpleDTO> productosDTO = productoService.listarDisponibles().stream()
                    .map(p -> new ProductoSimpleDTO(
                        p.getIdProducto(), p.getNombre(), p.getDescripcion(),
                        p.getPrecio(), p.getStock(),
                        p.getCategoria() != null ? p.getCategoria().getNombre() : "Sin categoría",
                        p.getProveedor() != null ? p.getProveedor().getNombre() : "Sin proveedor",
                        p.isActivo()
                    ))
                    .collect(Collectors.toList());
                model.addAttribute("productos", productosDTO);
            return "promociones";
        }

        try {
            promocionService.guardarPromocion(promocion);
            redirectAttributes.addFlashAttribute("mensaje", "Promoción guardada exitosamente");
            return "redirect:/promociones";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/promociones";
        }
    }

    @PostMapping("/actualizar")
    public String actualizarPromocion(@Valid @ModelAttribute("promocion") Promocion promocion,
                                      BindingResult result,
                                      Model model,
                                      RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            var promos = promocionService.obtenerPromociones();
            var dtoList = promos.stream().map(p -> {
                String tipo = "PORCENTAJE";
                if (p.getDescuento() == null) tipo = "";
                String estado;
                LocalDate hoy = LocalDate.now();
                if (!p.isActivo()) {
                    estado = "FINALIZADA";
                } else if (hoy.isBefore(p.getFechaInicio())) {
                    estado = "PROGRAMADA";
                } else if (hoy.isAfter(p.getFechaFin())) {
                    estado = "FINALIZADA";
                } else {
                    estado = "ACTIVA";
                }
                return new PromocionSimpleDTO(p.getIdPromocion(), p.getNombre(), p.getDescripcion(),
                        p.getDescuento(), p.getFechaInicio(), p.getFechaFin(), tipo, estado);
            }).collect(Collectors.toList());
                model.addAttribute("promociones", dtoList);
                List<ProductoSimpleDTO> productosDTO = productoService.listarDisponibles().stream()
                    .map(p -> new ProductoSimpleDTO(
                        p.getIdProducto(), p.getNombre(), p.getDescripcion(),
                        p.getPrecio(), p.getStock(),
                        p.getCategoria() != null ? p.getCategoria().getNombre() : "Sin categoría",
                        p.getProveedor() != null ? p.getProveedor().getNombre() : "Sin proveedor",
                        p.isActivo()
                    ))
                    .collect(Collectors.toList());
                model.addAttribute("productos", productosDTO);
            return "promociones";
        }

        try {
            promocionService.actualizar(promocion);
            redirectAttributes.addFlashAttribute("mensaje", "Promoción actualizada exitosamente");
            return "redirect:/promociones";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/promociones";
        }
    }

    @GetMapping("/finalizar/{id}")
    public String finalizarPromocion(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            promocionService.desactivar(id);
            redirectAttributes.addFlashAttribute("mensaje", "Promoción finalizada exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al finalizar promoción: " + e.getMessage());
        }
        return "redirect:/promociones";
    }
}
