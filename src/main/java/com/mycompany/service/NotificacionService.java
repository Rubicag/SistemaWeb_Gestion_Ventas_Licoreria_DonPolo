/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.service;

import com.mycompany.model.Notificacion;
import com.mycompany.repository.NotificacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class NotificacionService {
	@Autowired
	private NotificacionRepository notificacionRepository;

	public Notificacion guardar(Notificacion notificacion) {
		return notificacionRepository.save(notificacion);
	}

	public List<Notificacion> listarTodas() {
		return notificacionRepository.findAll();
	}

	public void marcarComoLeida(Long id) {
		Notificacion n = notificacionRepository.findById(id).orElse(null);
		if (n != null) {
			n.setLeida(true);
			notificacionRepository.save(n);
		}
	}
}
