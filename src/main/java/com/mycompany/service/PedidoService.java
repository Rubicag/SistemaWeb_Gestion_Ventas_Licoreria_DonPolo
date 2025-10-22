/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.service;

import com.mycompany.model.Pedido;
import com.mycompany.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PedidoService {
	@Autowired
	private PedidoRepository pedidoRepository;

	public Pedido guardarPedido(Pedido pedido) {
		return pedidoRepository.save(pedido);
	}

	public List<Pedido> listarPedidos() {
		return pedidoRepository.findAll();
	}

	public Pedido buscarPorId(Long id) {
		return pedidoRepository.findById(id).orElse(null);
	}
}
