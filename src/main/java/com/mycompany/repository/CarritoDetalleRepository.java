package com.mycompany.repository;

import com.mycompany.model.CarritoDetalle;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CarritoDetalleRepository extends JpaRepository<CarritoDetalle, Integer> {
    List<CarritoDetalle> findByIdCarrito(Integer idCarrito);
}
