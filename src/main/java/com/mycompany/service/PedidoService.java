package com.mycompany.service;

import com.mycompany.model.Pedido;
import com.mycompany.repository.PedidoRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PedidoService {
    private final PedidoRepository pedidoRepository;

    public PedidoService(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    public List<Pedido> obtenerPedidos() {
        return pedidoRepository.findAll();
    }
}
