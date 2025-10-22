/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.model;

import java.util.ArrayList;
import java.util.List;

public class Carrito {
	private List<CarritoItem> items = new ArrayList<>();

	public List<CarritoItem> getItems() {
		return items;
	}

	public void agregarItem(CarritoItem item) {
		// Si el producto ya está en el carrito, suma la cantidad
		for (CarritoItem ci : items) {
			if (ci.getProducto().getId().equals(item.getProducto().getId())) {
				ci.setCantidad(ci.getCantidad() + item.getCantidad());
				return;
			}
		}
		items.add(item);
	}

	public void eliminarItem(Integer productoId) {
		items.removeIf(ci -> ci.getProducto().getId().equals(productoId));
	}

	public double getTotal() {
		return items.stream().mapToDouble(CarritoItem::getSubtotal).sum();
	}

	public void vaciar() {
		items.clear();
	}
}
