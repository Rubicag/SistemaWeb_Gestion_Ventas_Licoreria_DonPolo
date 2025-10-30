package com.mycompany.service;

import com.mycompany.model.Carrito;
import com.mycompany.repository.CarritoRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CarritoService {
    private final CarritoRepository carritoRepository;

    public CarritoService(CarritoRepository carritoRepository) {
        this.carritoRepository = carritoRepository;
    }

    public List<Carrito> obtenerCarritos() {
        return carritoRepository.findAll();
    }

    public List<Carrito> obtenerCarritosPorUsuario(Integer idUsuario) {
        return carritoRepository.findByIdUsuario(idUsuario);
    }

    public Optional<Carrito> obtenerPorId(Integer id) {
        return carritoRepository.findById(id);
    }

    public Carrito guardarCarrito(Carrito carrito) {
        return carritoRepository.save(carrito);
    }

    public void eliminarCarrito(Integer id) {
        carritoRepository.deleteById(id);
    }
}
