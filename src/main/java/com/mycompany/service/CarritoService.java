/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.service;

/**
 *
 * @author LUIGGI
 */
import com.mycompany.model.Carrito;
import com.mycompany.repository.CarritoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CarritoService {
	@Autowired
	private CarritoRepository carritoRepository;

	public Carrito obtenerCarrito(String usuarioId) {
		return carritoRepository.obtenerCarrito(usuarioId);
	}

	public void guardarCarrito(String usuarioId, Carrito carrito) {
		carritoRepository.guardarCarrito(usuarioId, carrito);
	}

	public void eliminarCarrito(String usuarioId) {
		carritoRepository.eliminarCarrito(usuarioId);
	}
}
