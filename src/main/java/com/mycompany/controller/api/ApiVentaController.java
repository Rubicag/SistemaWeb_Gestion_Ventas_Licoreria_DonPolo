package com.mycompany.controller.api;

import com.mycompany.dto.VentaDTO;
import com.mycompany.model.Venta;
import com.mycompany.repository.VentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/ventas")
public class ApiVentaController {

    @Autowired
    private VentaRepository ventaRepository;

    private static final Logger logger = LoggerFactory.getLogger(ApiVentaController.class);

    @GetMapping
    public Page<VentaDTO> listar(Pageable pageable,
                                 @RequestParam(required = false) String search,
                                 @RequestParam(required = false) String fechaInicio,
                                 @RequestParam(required = false) String fechaFin,
                                 @RequestParam(required = false) Long vendedorId) {
        Pageable pg = pageable != null ? pageable : PageRequest.of(0, 25);
        // Build specification from params
        org.springframework.data.jpa.domain.Specification<com.mycompany.model.Venta> spec = (root, query, cb) -> cb.conjunction();
        try {
            java.time.LocalDateTime inicio = null, fin = null;
            if (fechaInicio != null && !fechaInicio.isEmpty()) inicio = java.time.LocalDate.parse(fechaInicio).atStartOfDay();
            if (fechaFin != null && !fechaFin.isEmpty()) fin = java.time.LocalDate.parse(fechaFin).atTime(23,59,59);
            spec = spec.and(com.mycompany.specification.VentaSpecification.fechaBetween(inicio, fin));
        } catch(Exception ex) {
            // ignore parse errors and continue
        }
        if (vendedorId != null) spec = spec.and(com.mycompany.specification.VentaSpecification.vendedorEquals(vendedorId.intValue()));
        if (search != null && !search.isEmpty()) spec = spec.and(com.mycompany.specification.VentaSpecification.textoEnClienteOMetodo(search));

        Page<Venta> ventas = ventaRepository.findAll(spec, pg);
        List<VentaDTO> content = ventas.stream().map(com.mycompany.mapper.VentaMapper::toDto).collect(Collectors.toList());
        var page = new PageImpl<>(content, pg, ventas.getTotalElements());
        logger.debug("/api/ventas listar -> page number={}, totalElements={}, contentSize={}", page.getNumber(), page.getTotalElements(), page.getContent().size());
        if (!page.getContent().isEmpty()) {
            VentaDTO sample = page.getContent().get(0);
            logger.debug("/api/ventas listar sample -> idVenta={}, comprobante={}, nombreCliente={}", sample.getIdVenta(), sample.getComprobante(), sample.getNombreCliente());
        }
        return page;
    }

    @GetMapping(path = "/{id}")
    public VentaDTO obtenerPorId(@org.springframework.web.bind.annotation.PathVariable("id") Integer id) {
        VentaDTO dto = ventaRepository.findById(id)
            .map(com.mycompany.mapper.VentaMapper::toDto)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Venta no encontrada"));
        logger.debug("/api/ventas/{} -> detalle -> idVenta={}, comprobante={}, total={}, detallesCount={}", id, dto.getIdVenta(), dto.getComprobante(), dto.getTotal(), dto.getDetalles() != null ? dto.getDetalles().size() : 0);
        return dto;
    }

    @GetMapping(path = "/summary")
    public org.springframework.data.domain.Page<com.mycompany.dto.VentaSummaryDTO> listarResumen(
            Pageable pageable,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String fechaInicio,
            @RequestParam(required = false) String fechaFin,
            @RequestParam(required = false) Long vendedorId) {
        java.time.LocalDateTime inicio = null, fin = null;
        try {
            if (fechaInicio != null && !fechaInicio.isEmpty()) inicio = java.time.LocalDate.parse(fechaInicio).atStartOfDay();
            if (fechaFin != null && !fechaFin.isEmpty()) fin = java.time.LocalDate.parse(fechaFin).atTime(23,59,59);
        } catch(Exception ex) {
            // ignore parse errors
        }
        Integer vend = vendedorId != null ? vendedorId.intValue() : null;
        try {
            return ventaRepository.findSummary(search, inicio, fin, vend, pageable != null ? pageable : PageRequest.of(0,25));
        } catch (Exception ex) {
            logger.error("Error al obtener resumen de ventas", ex);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al obtener resumen de ventas");
        }
    }

}
