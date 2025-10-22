/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.service;

import com.mycompany.model.Pago;
import com.mycompany.repository.PagoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PagoService {
	@Autowired
	private PagoRepository pagoRepository;

	public Pago guardarPago(Pago pago) {
		return pagoRepository.save(pago);
	}

	public List<Pago> listarPagos() {
		return pagoRepository.findAll();
	}

	public Pago buscarPorId(Long id) {
		return pagoRepository.findById(id).orElse(null);
	}
}
