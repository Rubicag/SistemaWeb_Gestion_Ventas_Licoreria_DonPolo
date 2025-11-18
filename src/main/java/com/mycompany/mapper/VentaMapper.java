package com.mycompany.mapper;

import com.mycompany.dto.VentaDTO;
import com.mycompany.model.Venta;

import java.util.stream.Collectors;

public class VentaMapper {

    public static VentaDTO toDto(Venta v) {
        if (v == null) return null;
        VentaDTO dto = new VentaDTO();
        dto.setIdVenta(v.getIdVenta());
        if (v.getUsuario() != null) {
            dto.setIdUsuario(v.getUsuario().getIdUsuario());
            dto.setNombreUsuario(v.getUsuario().getNombre());
        }
        if (v.getCliente() != null) {
            dto.setIdCliente(v.getCliente().getIdCliente());
            dto.setNombreCliente(v.getCliente().getNombreCompleto());
        }
        dto.setMetodoPago(v.getMetodoPago());
        dto.setTotal(v.getTotal());
        dto.setDescuento(v.getDescuento());
        dto.setComprobante(v.getComprobante());
        dto.setEstado(v.getEstado());
        dto.setObservaciones(v.getObservaciones());
        dto.setFecha(v.getFecha());
        dto.setIdCarrito(v.getIdCarrito());
        if (v.getDetalles() != null) {
            dto.setDetalles(v.getDetalles().stream().map(d -> {
                com.mycompany.dto.DetalleVentaDTO dd = new com.mycompany.dto.DetalleVentaDTO();
                dd.setIdDetalle(d.getIdDetalle());
                dd.setIdProducto(d.getProducto() != null ? d.getProducto().getIdProducto() : null);
                dd.setNombreProducto(d.getProducto() != null ? d.getProducto().getNombre() : null);
                dd.setCantidad(d.getCantidad());
                dd.setPrecioUnitario(d.getPrecioUnitario());
                dd.setDescuento(d.getDescuento());
                dd.setSubtotal(d.getSubtotal());
                return dd;
            }).collect(Collectors.toList()));
        }
        return dto;
    }
}
