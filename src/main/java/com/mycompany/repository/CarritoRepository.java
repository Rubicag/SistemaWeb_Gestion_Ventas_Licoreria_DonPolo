/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.repository;

/**
 *
 * @author LUIGGI
 */
import com.mycompany.model.Carrito;
import org.springframework.stereotype.Repository;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

@Repository
public class CarritoRepository {
	private final Map<String, Carrito> carritoPorUsuario = new ConcurrentHashMap<>();

	public Carrito obtenerCarrito(String usuarioId) {
		return carritoPorUsuario.get(usuarioId);
	}

	public void guardarCarrito(String usuarioId, Carrito carrito) {
		carritoPorUsuario.put(usuarioId, carrito);
	}

	public void eliminarCarrito(String usuarioId) {
		carritoPorUsuario.remove(usuarioId);
	}
}
