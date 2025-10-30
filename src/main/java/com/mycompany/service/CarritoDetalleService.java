package com.mycompany.service;

import com.mycompany.model.CarritoDetalle;
import com.mycompany.repository.CarritoDetalleRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CarritoDetalleService {
    private final CarritoDetalleRepository carritoDetalleRepository;

    public CarritoDetalleService(CarritoDetalleRepository carritoDetalleRepository) {
        this.carritoDetalleRepository = carritoDetalleRepository;
    }

    public List<CarritoDetalle> obtenerDetallesPorCarrito(Integer idCarrito) {
        return carritoDetalleRepository.findByIdCarrito(idCarrito);
    }

    public Optional<CarritoDetalle> obtenerPorId(Integer id) {
        return carritoDetalleRepository.findById(id);
    }

    public CarritoDetalle guardarDetalle(CarritoDetalle detalle) {
        return carritoDetalleRepository.save(detalle);
    }

    public void eliminarDetalle(Integer id) {
        carritoDetalleRepository.deleteById(id);
    }
}
