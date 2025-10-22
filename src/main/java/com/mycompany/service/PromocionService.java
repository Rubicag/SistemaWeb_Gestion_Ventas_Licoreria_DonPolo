/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.service;

import com.mycompany.model.Promocion;
import com.mycompany.repository.PromocionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Date;

@Service
public class PromocionService {
	@Autowired
	private PromocionRepository promocionRepository;

	public Promocion guardar(Promocion promocion) {
		return promocionRepository.save(promocion);
	}

	public List<Promocion> listarTodas() {
		return promocionRepository.findAll();
	}

	public Promocion buscarPorId(Long id) {
		return promocionRepository.findById(id).orElse(null);
	}

	@SuppressWarnings("unlikely-arg-type")
    public Promocion buscarPromocionActivaPorProducto(Long productoId) {
		Date hoy = new Date();
		return promocionRepository.findAll().stream()
			.filter(p -> p.getProducto().getId().equals(productoId)
				&& !hoy.before(p.getFechaInicio())
				&& !hoy.after(p.getFechaFin()))
			.findFirst().orElse(null);
	}
}
