package com.mycompany.service;

import com.mycompany.model.PedidoDetalle;
import com.mycompany.repository.PedidoDetalleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PedidoDetalleService {
    @Autowired
    private PedidoDetalleRepository pedidoDetalleRepository;

    public PedidoDetalle guardarDetalle(PedidoDetalle detalle) {
        return pedidoDetalleRepository.save(detalle);
    }

    public List<PedidoDetalle> listarDetalles() {
        return pedidoDetalleRepository.findAll();
    }
}
