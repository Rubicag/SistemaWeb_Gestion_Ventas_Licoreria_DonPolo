package com.mycompany.repository;

import com.mycompany.model.Carrito;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CarritoRepository extends JpaRepository<Carrito, Integer> {
    List<Carrito> findByIdUsuario(Integer idUsuario);
}
