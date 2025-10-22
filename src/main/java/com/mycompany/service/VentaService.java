/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.service;

import com.mycompany.model.Venta;
import com.mycompany.repository.VentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class VentaService {

	@Autowired
	private VentaRepository ventaRepository;

	public List<Venta> listarTodos() {
		return ventaRepository.findAll();
	}

	public void guardar(Venta venta) {
		ventaRepository.save(venta);
	}

	public Venta buscarPorId(Long id) {
		return ventaRepository.findById(id).orElse(null);
	}

	public void eliminar(Long id) {
		ventaRepository.deleteById(id);
	}
}